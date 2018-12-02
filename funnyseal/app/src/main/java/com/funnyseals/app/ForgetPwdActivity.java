package com.funnyseals.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.funnyseals.app.model.UserDao;
import com.mob.MobSDK;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ForgetPwdActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEtAccount;
    private EditText mEtPassword;
    private EditText mEtPasswordAgain;
    private EditText mEtIdentifyingCode;
    private Button   mBtnCodeSend;
    private Button   mBtnChangePwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);

        initViews();
        initEvents();
        initSDK();
    }

    private void initSDK() {
        MobSDK.init(this);
    }

    private void initViews() {
        mEtAccount = findViewById(R.id.et_forgetPwd_AccountInput);
        mEtPassword = findViewById(R.id.et_forgetpwd_PasswordInput);
        mEtPasswordAgain = findViewById(R.id.et_forgetpwd_PasswordInputAgain);
        mEtIdentifyingCode = findViewById(R.id.et_forgetpwd_IdentifyingCode);
        mBtnCodeSend = findViewById(R.id.btn_forgetPwd_CodeSend);
        mBtnChangePwd = findViewById(R.id.btn_forgetPwd_ChangePwd);
    }

    private void initEvents() {
        mBtnCodeSend.setOnClickListener(this);
        mBtnChangePwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_forgetPwd_CodeSend:
                SendSMS();
                break;
            case R.id.btn_forgetPwd_ChangePwd:
                changePwd();
                break;
        }
    }

    EventHandler sendSMSHandler = new EventHandler() {
        public void afterEvent(int event, int result, Object data) {
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            new Handler(Looper.getMainLooper(), msg1 -> {
                int event1 = msg1.arg1;
                int result1 = msg1.arg2;
                Object data1 = msg1.obj;
                if (event1 == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    if (result1 == SMSSDK.RESULT_COMPLETE) {
                        showToast("验证码已发送");
                    } else {
                        showToast("输入的手机号不正确，请检查！");
                    }
                } else if (event1 == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    if (result1 == SMSSDK.RESULT_COMPLETE) {
                        updateDB();
                    } else {
                        showToast("验证码不正确！");
                    }
                }
                return false;
            }).sendMessage(msg);
        }
    };

    public void SendSMS() {
        if (TextUtils.isEmpty(mEtAccount.getText())) {
            showToast("手机号不能为空");
            return;
        } else if (mEtAccount.getText().length() != 11) {
            showToast("输入的手机号不正确，请检查！");
            return;
        }
        SMSSDK.registerEventHandler(sendSMSHandler);
        SMSSDK.getVerificationCode("86", mEtAccount.getText().toString());
    }

    public void changePwd() {
        if (mEtPassword.getText().toString().length() < 6 || mEtPassword.getText().toString().length() > 16) {
            showToast("请输入6-16位密码");
        } else if (!mEtPassword.getText().toString().equals(mEtPasswordAgain.getText().toString())) {
            showToast("两次输入的密码不同");
        } else {
            SMSSDK.submitVerificationCode("86", mEtAccount.getText().toString(), mEtIdentifyingCode.getText().toString());
        }
    }

    public void updateDB() {
        new Thread(() -> {
            try {
                Connection conn = UserDao.getConnection();
                if (conn != null) {
                    PreparedStatement statement = conn.prepareStatement("select * from hz where HZ_ZH=?");
                    statement.setString(1, mEtAccount.getText().toString());
                    ResultSet rs = statement.executeQuery();
                    rs.last();
                    if (rs.getRow() == 0) {
                        statement = conn.prepareStatement("select * from ys where YS_ZH=?");
                        statement.setString(1, mEtAccount.getText().toString());
                        rs = statement.executeQuery();
                        rs.last();
                        if (rs.getRow() == 0) {
                            showToast("此用户不存在！");
                        } else {
                            statement = conn.prepareStatement("update ys set YS_MM=? where YS_ZH=?");
                            statement.setString(1, mEtPassword.getText().toString());
                            statement.setString(2, mEtAccount.getText().toString());
                            statement.executeUpdate();
                            showToast("修改成功！");
                        }
                    } else {
                        statement = conn.prepareStatement("update hz set YS_MM=? where YS_ZH=?");
                        statement.setString(1, mEtPassword.getText().toString());
                        statement.setString(2, mEtAccount.getText().toString());
                        statement.executeUpdate();
                        showToast("修改成功！");
                    }
                    conn.close();
                    statement.close();
                    rs.close();
                } else {
                    // 输出连接信息
                    showToast("数据库连接失败！");
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void showToast(final String msg) {
        runOnUiThread(() -> Toast.makeText(ForgetPwdActivity.this, msg, Toast.LENGTH_SHORT).show());
    }
}
