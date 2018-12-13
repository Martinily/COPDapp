package com.funnyseals.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.feature.bottomtab.DoctorBottomActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initEvents();
    }

    private void initViews() {
        mEtAccount = findViewById(R.id.et_login_AccountInput);
        mEtPassword = findViewById(R.id.et_login_PasswordInput);
        mBtnLogin = findViewById(R.id.btn_login_login);
        mLinkSignup = findViewById(R.id.link_signup);
        mLinkForgetPwd = findViewById(R.id.link_register);
    }

    private void initEvents() {
        mBtnLogin.setOnClickListener(this);
        mLinkSignup.setOnClickListener(this);
        mLinkForgetPwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_login:
                login();
                break;
            case R.id.link_signup:
                startActivity(new Intent(this, SignupActivity.class));
                break;
            case R.id.link_register:
                startActivity(new Intent(this, ForgetPwdActivity.class));
                break;
        }
    }

    private void login() {
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
            new Thread(() -> {
                String send = "";
                Socket socket;
                //try {
                    /*JSONObject jsonObject = new JSONObject();
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
                            showToast("登录成功！");*/
                            MyApplication application = (MyApplication) getApplication();
                            application.setAccount(getAccount());
                            /*switch (jsonObject.getString("user_type")) {
                                case "d":*/
                                    startActivity(new Intent(LoginActivity.this, DoctorBottomActivity.class));
                                    finish();
                                    /*break;
                                case "p":
                                    startActivity(new Intent(LoginActivity.this, PatientBottomActivity.class));
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
                    socket.close();*/
                //} catch (IOException | JSONException e) {
                //    e.printStackTrace();
                //}
                progressDialog.dismiss();
            }).start();
        }
    }

    public String getAccount() {
        return mEtAccount.getText().toString().trim();
    }

    public String getPassword() {
        return mEtPassword.getText().toString().trim();
    }

    public void showToast(final String msg) {
        runOnUiThread(() -> Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show());
    }
}
