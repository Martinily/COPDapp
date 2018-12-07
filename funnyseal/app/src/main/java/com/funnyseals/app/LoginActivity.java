package com.funnyseals.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.feature.bottomtab.DoctorBottomActivity;
import com.funnyseals.app.feature.bottomtab.PatientBottomActivity;
import com.funnyseals.app.model.UserDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
登录界面
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEtAccount;
    private EditText mEtPassword;
    private Button   mBtnLogin;
    private Button   mBtnSignup;
    private Button   mBtnForgetPwd;
    private Context  context;

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
                try {
                    Connection conn = UserDao.getConnection();
                    if (conn != null) {
                        PreparedStatement statement = null;
                        statement = conn.prepareStatement("select YS_MM from ys where YS_ZH=?");
                        statement.setString(1, mEtAccount.getText().toString());
                        ResultSet rs = statement.executeQuery();
                        rs.last();
                        if (rs.getRow() == 0) {
                            statement = conn.prepareStatement("select HZ_MM from hz where HZ_ZH=?");
                            statement.setString(1, mEtAccount.getText().toString());
                            rs = statement.executeQuery();
                            rs.last();
                            if (rs.getRow() == 0) {
                                showToast("此用户不存在！");
                            } else if (rs.getString(1).equals(mEtPassword.getText().toString())) {
                                /*EMClient.getInstance().login(mEtAccount.getText().toString(), mEtPassword.getText().toString(), new EMCallBack() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError(int code, String error) {
                                    }

                                    @Override
                                    public void onProgress(int progress, String status) {
                                    }
                                });*/
                                ((MyApplication) getBaseContext().getApplicationContext()).setAccount(mEtAccount.getText().toString());
                                conn.close();
                                statement.close();
                                rs.close();
                                startActivity(new Intent(LoginActivity.this, PatientBottomActivity.class));

                                finish();
                            }
                        } else if (rs.getString(1).equals(mEtPassword.getText().toString())) {
                            /*EMClient.getInstance().login(mEtAccount.getText().toString(), mEtPassword.getText().toString(), new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onError(int code, String error) {
                                }

                                @Override
                                public void onProgress(int progress, String status) {
                                }
                            });*/
                            ((MyApplication) getBaseContext().getApplicationContext()).setAccount(mEtAccount.getText().toString());
                            conn.close();
                            statement.close();
                            rs.close();
                            startActivity(new Intent(LoginActivity.this, DoctorBottomActivity.class));
                            finish();
                        }

                    } else {
                        // 输出连接信息
                        showToast("数据库连接失败！");
                    }
                } catch (ClassNotFoundException | SQLException e) {
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
