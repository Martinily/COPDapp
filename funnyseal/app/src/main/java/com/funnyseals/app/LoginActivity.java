package com.funnyseals.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.funnyseals.app.feature.bottomtab.DoctorBottomActivity;
import com.funnyseals.app.feature.bottomtab.PatientBottomActivity;
import com.funnyseals.app.util.SocketUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEtAccount;
    private EditText mEtPassword;
    private Button   mBtnLogin;
    private Button   mBtnSignup;
    private Button   mBtnForgetPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        startActivity(new Intent(LoginActivity.this,PatientBottomActivity.class));
        //initViews();
        //initEvents();
    }

    private void initViews() {
        mEtAccount = findViewById(R.id.et_login_AccountInput);
        mEtPassword = findViewById(R.id.et_login_PasswordInput);
        mBtnLogin = findViewById(R.id.btn_login_login);
        mBtnSignup = findViewById(R.id.btn_login_signup);
        mBtnForgetPwd = findViewById(R.id.btn_login_forgetpwd);
    }

    private void initEvents() {
        mBtnLogin.setOnClickListener(this);
        mBtnSignup.setOnClickListener(this);
        mBtnForgetPwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_login:
                login();
                break;
            case R.id.btn_login_signup:
                startActivity(new Intent(this, SignupActivity.class));
                break;
            case R.id.btn_login_forgetpwd:
                startActivity(new Intent(this, ForgetPwdActivity.class));
                break;
        }
    }

    private void login() {
        if (getAccount().isEmpty()) {
            showToast("请输入账号！");
        } else if (getPassword().isEmpty()) {
            showToast("请输入密码！");
        } else {
            new Thread(() -> {
                String send="";
                Socket socket=null;
                try{
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("request_type","1");
                    jsonObject.put("user_name",getAccount());
                    jsonObject.put("user_pw",getPassword());
                    send=jsonObject.toString();
                    socket = SocketUtil.getSendSocket();
                    DataOutputStream out=new DataOutputStream(socket.getOutputStream());
                    out.writeUTF(send);
                    out.close();

                    socket = SocketUtil.getGetSocket();
                    DataInputStream datainputstream=new DataInputStream(socket.getInputStream());
                    String message=datainputstream.readUTF();

                    jsonObject=new JSONObject(message);
                    switch (jsonObject.getString("login_state")) {
                        case "成功":
                            showToast("登录成功！");
                            switch (jsonObject.getString("user_type")){
                                case "d":
                                    startActivity(new Intent(LoginActivity.this, DoctorBottomActivity.class));
                                    finish();
                                    break;
                                case "p":
                                    startActivity(new Intent(LoginActivity.this,PatientBottomActivity.class));
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
