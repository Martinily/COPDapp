package com.funnyseals.app;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.funnyseals.app.util.SocketUtil;
import com.funnyseals.app.util.TimeDownUtil;
import com.mob.MobSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ForgetPwdActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String REGEX_MOBILE   = "^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57]|19[0-9]|16[0-9])[0-9]{8}$";
    private static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,20}$";

    private EditText     mEtAccount;
    private EditText     mEtPassword;
    private EditText     mEtPasswordAgain;
    private EditText     mEtIdentifyingCode;
    private Button       mBtnCodeSend;
    private Button       mBtnChangePwd;
    private TimeDownUtil timeDownUtil;

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
        timeDownUtil = new TimeDownUtil(180000, 1000, mBtnCodeSend);
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

    private EventHandler sendSMSHandler = new EventHandler() {
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
        } else if (!Pattern.matches(REGEX_MOBILE, mEtAccount.getText().toString())) {
            showToast("输入的手机号不正确，请检查！");
            return;
        }
        SMSSDK.registerEventHandler(sendSMSHandler);
        SMSSDK.getVerificationCode("86", mEtAccount.getText().toString());
        timeDownUtil.start();
    }

    public void changePwd() {
        if (!Pattern.matches(REGEX_PASSWORD, mEtPassword.getText().toString())) {
            showToast("请输入6-20位由大小写字母和数字组成的密码！");
        } else if (!mEtPassword.getText().toString().equals(mEtPasswordAgain.getText().toString())) {
            showToast("两次输入的密码不同");
        } else {
            SMSSDK.submitVerificationCode("86", mEtAccount.getText().toString(), mEtIdentifyingCode.getText().toString());
        }
    }

    public void updateDB() {

        final ProgressDialog progressDialog = new ProgressDialog(ForgetPwdActivity.this,
                R.style.AppTheme_Dark_Dialog);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("正在修改密码。。。");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        new Thread(() -> {
            String send = "";
            Socket socket;
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("request_type", "8");
                jsonObject.put("modify_type", "forget");
                jsonObject.put("user_name", mEtAccount.getText().toString());
                jsonObject.put("user_pw", mEtPassword.getText().toString());
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
                switch (jsonObject.getString("password_result")) {
                    case "true":
                        showToast("密码修改成功！");
                        startActivity(new Intent(ForgetPwdActivity.this, LoginActivity.class));
                        finish();
                        break;
                    case "false":
                        showToast("密码修改失败！");
                        break;
                }
                socket.close();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }).start();
    }

    public void showToast(final String msg) {
        runOnUiThread(() -> Toast.makeText(ForgetPwdActivity.this, msg, Toast.LENGTH_SHORT).show());
    }
}
