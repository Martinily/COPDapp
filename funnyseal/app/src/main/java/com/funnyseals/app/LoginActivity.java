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
import com.hyphenate.chat.EMClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Pattern;

import butterknife.BindView;

/**
 *
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,20}$";

    @BindView(R.id.et_login_AccountInput)
    EditText mEtAccount;
    @BindView(R.id.et_login_PasswordInput)
    EditText mEtPassword;
    @BindView(R.id.btn_login_login)
    Button   mBtnLogin;
    @BindView(R.id.link_signup)
    TextView mLinkSignup;
    @BindView(R.id.link_forgetPWD)
    TextView mLinkForgetPwd;
    @BindView(R.id.remember_password)
    CheckBox mCbRememberPassword;

    private SharedPreferences        pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        initEvents();
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
            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme_Dark_Dialog);

            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("正在登录。。。");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            loginEM();
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
    }

    public void loginEM () {
        EMClient.getInstance().login(getAccount(), getPassword(), new EMCallBack() {
            @Override
            public void onSuccess () {

            }

            @Override
            public void onError (int code, String error) {

            }

            @Override
            public void onProgress (int progress, String status) {

            }
        });
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
