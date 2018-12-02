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
import android.widget.RadioButton;
import android.widget.Toast;

import com.funnyseals.app.model.UserDao;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.mob.MobSDK;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText    mEtAccount;
    private EditText    mEtPassword;
    private EditText    mEtPasswordAgain;
    private EditText    mEtIdentifyingCode;
    private Button      mBtnCodeSend;
    private RadioButton mRbAccountTypePatient;
    private RadioButton mRbAccountTypeDoctor;
    private Button      mBtnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initViews();
        initEvents();
        initSDK();
    }

    private void initViews() {
        mEtAccount = findViewById(R.id.et_signup_AccountInput);
        mEtPassword = findViewById(R.id.et_signup_PasswordInput);
        mEtPasswordAgain = findViewById(R.id.et_signup_PasswordInputAgain);
        mEtIdentifyingCode = findViewById(R.id.et_signup_IdentifyingCode);
        mBtnCodeSend = findViewById(R.id.btn_signup_CodeSend);
        mRbAccountTypePatient = findViewById(R.id.rb_signup_accountTypePatient);
        mRbAccountTypeDoctor = findViewById(R.id.rb_signup_accountTypeDoctor);
        mBtnSignup = findViewById(R.id.btn_signup_signup);
    }

    private void initEvents() {
        mBtnCodeSend.setOnClickListener(this);
        mBtnSignup.setOnClickListener(this);
    }

    private void initSDK() {
        MobSDK.init(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signup_CodeSend:
                sendSMS();
                break;
            case R.id.btn_signup_signup:
                signup();
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
                        writeIntoDB();
                    } else {
                        showToast("验证码不正确！");
                    }
                }
                return false;
            }).sendMessage(msg);
        }
    };

    public void sendSMS() {
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

    public void destorySendSMSHandler() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(sendSMSHandler);
    }

    public void writeIntoDB() {
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
                            if (mRbAccountTypeDoctor.isChecked()) {
                                statement = conn.prepareStatement("insert into YS(YS_ZH,YS_MM) values(?,?)");
                            } else {
                                statement = conn.prepareStatement("insert into YS(HZ_ZH,HZ_MM) values(?,?)");
                            }
                            statement.setString(1, mEtAccount.getText().toString());
                            statement.setString(m  2, mEtPassword.getText().toString());
                            statement.executeUpdate();
                            EMClient.getInstance().createAccount(mEtAccount.getText().toString(), mEtPassword.getText().toString());
                            showToast("注册完成");
                        } else {
                            showToast("账号已存在");
                        }
                    }
                    conn.close();
                    statement.close();
                    rs.close();
                    destorySendSMSHandler();
                } else {
                    // 输出连接信息
                    showToast("数据库连接失败！");
                }
            } catch (ClassNotFoundException | SQLException | HyphenateException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void signup() {
        if (mEtPassword.getText().toString().length() < 6 || mEtPassword.getText().toString().length() > 16) {
            showToast("请输入6-16位密码");
        } else if (!mEtPassword.getText().toString().equals(mEtPasswordAgain.getText().toString())) {
            showToast("两次输入的密码不同");
        } else {
            SMSSDK.submitVerificationCode("86", mEtAccount.getText().toString(), mEtIdentifyingCode.getText().toString());
        }
    }

    public void showToast(final String msg) {
        runOnUiThread(() -> Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_SHORT).show());
    }
}
