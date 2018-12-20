package com.funnyseals.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.feature.bottomtab.DoctorBottomActivity;
import com.funnyseals.app.feature.bottomtab.PatientBottomActivity;
import com.funnyseals.app.model.User;
import com.funnyseals.app.util.BtnClickLimitUtil;
import com.funnyseals.app.util.SocketUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Pattern;

/**
 *
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,20}$";

    private EditText mEtAccount;
    private EditText mEtPassword;
    private Button   mBtnLogin;
    private TextView mLinkSignup;
    private TextView mLinkForgetPwd;
    private CheckBox mCbRememberPassword;

    private SharedPreferences        pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        initView();
        initEvents();
    }

    private void initView () {
        mEtAccount = findViewById(R.id.et_login_AccountInput);
        mEtPassword = findViewById(R.id.et_login_PasswordInput);
        mBtnLogin = findViewById(R.id.btn_login_login);
        mLinkSignup = findViewById(R.id.link_signup);
        mLinkForgetPwd = findViewById(R.id.link_forgetPWD);
        mCbRememberPassword = findViewById(R.id.remember_password);
    }

    private void initEvents () {
        mBtnLogin.setOnClickListener(this);
        mLinkSignup.setOnClickListener(this);
        mLinkForgetPwd.setOnClickListener(this);

        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            mEtAccount.setText(account);
            mEtPassword.setText(password);
            mCbRememberPassword.setChecked(true);
        }
    }

    @Override
    public void onClick (View v) {
        if (BtnClickLimitUtil.isFastClick()) {
            switch (v.getId()) {
                case R.id.btn_login_login:
                    linkToServer();
                    break;
                case R.id.link_signup:
                    startActivity(new Intent(this, SignupActivity.class));
                    break;
                case R.id.link_forgetPWD:
                    startActivity(new Intent(this, ForgetPwdActivity.class));
                    break;
            }
        }
    }

    private void linkToServer () {
        if (getAccount().isEmpty()) {
            showToast("请输入账号！");
        } else if (!Pattern.matches(REGEX_PASSWORD, mEtPassword.getText().toString())) {
            showToast("请输入6-20位由大小写字母和数字组成的密码！");
        } else {
            EMClient.getInstance().login(getAccount(), getPassword(), new EMCallBack() {
                @Override
                public void onSuccess () {
                    runOnUiThread(() -> Login());
                }

                @Override
                public void onError (int code, String error) {
                    runOnUiThread(() -> {
                        String err;
                        switch (code) {
                            // 网络异常 2
                            case EMError.NETWORK_ERROR:
                                err="网络错误 code: " + code + ", message:" + error;
                                break;
                            // 无效的用户名 101
                            case EMError.INVALID_USER_NAME:
                                err="无效的用户名 code: " + code + ", message:" + error;
                                break;
                            // 无效的密码 102
                            case EMError.INVALID_PASSWORD:
                                err="无效的密码 code: " + code + ", message:" + error;
                                break;
                            // 用户认证失败，用户名或密码错误 202
                            case EMError.USER_AUTHENTICATION_FAILED:
                                err="用户认证失败，用户名或密码错误 code: " + code + ", message:" + error;
                                break;
                            // 用户不存在 204
                            case EMError.USER_NOT_FOUND:
                                err="用户不存在 code: " + code + ", message:" + error;
                                break;
                            // 无法访问到服务器 300
                            case EMError.SERVER_NOT_REACHABLE:
                                err="无法访问到服务器 code: " + code + ", message:" + error;
                                break;
                            // 等待服务器响应超时 301
                            case EMError.SERVER_TIMEOUT:
                                err="等待服务器响应超时 code: " + code + ", message:" + error;
                                break;
                            // 服务器繁忙 302
                            case EMError.SERVER_BUSY:
                                err="服务器繁忙 code: " + code + ", message:" + error;
                                break;
                            // 未知 Server 异常 303 一般断网会出现这个错误
                            case EMError.SERVER_UNKNOWN_ERROR:
                                err="未知的服务器异常 code: " + code + ", message:" + error;
                                break;
                            default:
                                err="ml_sign_in_failed code: " + code + ", message:";
                                break;
                        }
                        showToast(err);
                    });
                }

                @Override
                public void onProgress (int progress, String status) {
                    runOnUiThread(() -> showToast("网络连接失败！"));
                }
            });
        }
    }

    public void Login () {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("正在登录。。。");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        new Thread(() -> {
            String send;
            Socket socket;
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("request_type", "1");
                jsonObject.put("user_name", getAccount());
                jsonObject.put("user_pw", getPassword());
                send = jsonObject.toString();
                socket = SocketUtil.getSendSocket();
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(send);
                out.close();

                socket = SocketUtil.getGetSocket();
                DataInputStream datainputstream = new DataInputStream(socket.getInputStream());
                String message = datainputstream.readUTF();

                jsonObject = new JSONObject(message);
                progressDialog.dismiss();
                switch (jsonObject.getString("login_state")) {
                    case "成功":
                        showToast("登录成功！");
                        rememberPassword();

                        MyApplication myApplication = (MyApplication) getApplication();
                        myApplication.setAccount(getAccount());

                        switch (jsonObject.getString("user_type")) {
                            case "d":
                                myApplication.setUser(
                                        new User(jsonObject.getString("docID"),
                                                jsonObject.getString("docName"),
                                                jsonObject.getString("docSex"),
                                                Integer.valueOf(jsonObject.getString("docAge")),
                                                jsonObject.getString("docTime"),
                                                jsonObject.getString("docAddress"),
                                                jsonObject.getString("docCompany"),
                                                jsonObject.getString("docTitle")));
                                startActivity(new Intent(LoginActivity.this,
                                        DoctorBottomActivity.class));
                                finish();
                                break;
                            case "p":
                                myApplication.setUser(
                                        new User(jsonObject.getString("pID"),
                                                jsonObject.getString("pName"),
                                                jsonObject.getString("pSex"),
                                                Integer.valueOf(jsonObject.getString("pAge")),
                                                jsonObject.getString("pTime"),
                                                jsonObject.getString("pAddress"),
                                                jsonObject.getString("HistoryCondition"),
                                                jsonObject.getString("HistoryAdvice"),
                                                jsonObject.getString("docID")));
                                startActivity(new Intent(LoginActivity.this,
                                        PatientBottomActivity.class));
                                finish();
                                break;
                        }
                        break;
                    case "用户不存在":
                        showToast("该用户不存在！");
                        break;
                    case "密码错误":
                        showToast("密码错误！");
                        break;
                }
                socket.close();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
            Thread.interrupted();
        }).start();
    }

    public void rememberPassword () {
        editor = pref.edit();
        if (mCbRememberPassword.isChecked()) {
            editor.putBoolean("remember_password", true);
            editor.putString("account", getAccount());
            editor.putString("password", getPassword());
        } else {
            editor.clear();
        }
        editor.apply();
    }

    public String getAccount () {
        return mEtAccount.getText().toString().trim();
    }

    public String getPassword () {
        return mEtPassword.getText().toString().trim();
    }

    public void showToast (final String msg) {
        runOnUiThread(() -> Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show());
    }
}
