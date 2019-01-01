package com.funnyseals.app.feature.patientPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.funnyseals.app.LoginActivity;
import com.funnyseals.app.R;
import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.feature.doctorPersonalCenter.DoctorPasswordActivity;
import com.funnyseals.app.util.SocketUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 修改密码界面
 */
public class PatientPasswordActivity extends AppCompatActivity {
    private Button      bt_patient_change_complete,ib_patient_change_return;
    private EditText    et_patient_change_oldpassword, et_patient_change_newpassword,
            et_patient_change_againpassword;
    private MyApplication myApplication;
    private String      result="";
    private boolean       type = false;

    /**
     * 判定密码不包含特殊字符
     * 判定两次密码一致
     */
    public static boolean isLetterOrDigit (String str) {
        boolean isLetterOrDigit = false;
        for (int i = 0; i < str.length(); i++)
            if (Character.isLetterOrDigit(str.charAt(i))) {
                isLetterOrDigit = true;
            }
        String regex = "^[a-zA-Z0-9]{6,20}$";
        return (isLetterOrDigit && str.matches(regex));
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_password);
        init();

    }

    /**
     * 密码合法性判断
     * 不能为空
     * 不能包含特殊字符
     * 长度为6-20
     * 两次密码一致
     */
    public void myCorrectPas () {

        if (getOldpassword().isEmpty() || getNewpassword().isEmpty() || getAgainpassword()
                .isEmpty()) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();

        } else if (!isLetterOrDigit(getNewpassword())) {
            Toast.makeText(this, "密码不能包含特殊字符", Toast.LENGTH_SHORT).show();

        } else if (getNewpassword().length() < 6 || getNewpassword().length() > 16) {
            Toast.makeText(this, "密码长度应为6-20位", Toast.LENGTH_SHORT).show();

        } else if (!isSame(getNewpassword(), getAgainpassword())) {
            Toast.makeText(this, "两次密码不一致，请重新输入", Toast.LENGTH_LONG).show();

        } else {
            changePassword();
        }
    }

    /**
     * 初始化控件
     */
    private void init () {
        //获取各个edittext值
        et_patient_change_oldpassword = findViewById(R.id.et_patient_change_oldpassword);
        et_patient_change_newpassword = findViewById(R.id.et_patient_change_newpassword);
        et_patient_change_againpassword = findViewById(R.id.et_patient_change_againpassword);
        myApplication = (MyApplication) getApplication();

        bt_patient_change_complete = findViewById(R.id.bt_patient_change_complete);
        ib_patient_change_return = findViewById(R.id.ib_patient_change_return);
        bt_patient_change_complete.setOnClickListener(new addListener());
        ib_patient_change_return.setOnClickListener(new addListener());
    }

    /**
     *连接服务器
     */
    public void changePassword(){
        Thread thread  = new Thread(() -> {
            String send = "";
            Socket socket;
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("request_type", "8");
                jsonObject.put("modify_type", "update");
                jsonObject.put("ID", myApplication.getAccount());
                jsonObject.put("oldPassword", getOldpassword());
                jsonObject.put("newPassword", getNewpassword());
                send = jsonObject.toString();
                socket = SocketUtil.getSendSocket();
                DataOutputStream out = new DataOutputStream(socket
                        .getOutputStream());
                out.writeUTF(send);
                out.close();

                Thread.sleep(2000);

                socket = SocketUtil.setPort(2032);
                DataInputStream dataInputStream = new DataInputStream(socket
                        .getInputStream());
                String message = dataInputStream.readUTF();

                JSONObject jsonObject1 = new JSONObject(message);


                result=jsonObject1.getString("password_result");
                socket.close();
            } catch (IOException | JSONException | InterruptedException e) {
                e.printStackTrace();
            }
            Thread.interrupted();
        });
        thread.start();
        while (thread.isAlive()){

        }
        switch (result) {
            case "0":
                Toast.makeText(PatientPasswordActivity.this, "修改密码成功",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PatientPasswordActivity.this, LoginActivity
                        .class);
                startActivity(intent);
                break;
            case "1":
                Toast.makeText(PatientPasswordActivity.this, "用户名错误",
                        Toast.LENGTH_LONG).show();
                break;
            case "2":
                Toast.makeText(PatientPasswordActivity.this, "密码错误", Toast
                        .LENGTH_LONG).show();

                break;
            case "3":
                Toast.makeText(PatientPasswordActivity.this, "当前网络不稳定，换个姿势试试~", Toast
                        .LENGTH_LONG).show();
                break;
        }
    }
    public boolean isSame (String str1, String str2) {
        boolean isSame = false;
        if (str1.equals(str2)) {
            isSame = true;
        }
        return isSame;
    }

    /**
     * 返回按钮提示
     * 确认 返回个人信息
     * 取消 停留当前页面
     */
    public void Sure () {
        AlertDialog.Builder builder = new AlertDialog.Builder(PatientPasswordActivity.this);
        builder.setMessage("密码未修改，确定退出？");
        builder.setPositiveButton("确定", (dialog, which) -> {
            //   dialog.dismiss();
            finish();
        })
                .setNegativeButton("取消", (dialog, which) -> {
                    //取消对话框，返回界面
                    dialog.cancel();
                }).create().show();
        //只有点击按钮才行，点击空白无用
        builder.setCancelable(false);
    }

    /**
     * 获取
     * 旧密码
     * 新密码
     * 再次输入的新密码
     * 提示函数
     */
    public String getOldpassword () {
        return et_patient_change_oldpassword.getText().toString().trim();
    }

    public String getNewpassword () {
        return et_patient_change_newpassword.getText().toString().trim();
    }

    public String getAgainpassword () {
        return et_patient_change_againpassword.getText().toString().trim();
    }

    public void showToast (final String msg) {
        runOnUiThread(() -> Toast.makeText(PatientPasswordActivity.this, msg, Toast.LENGTH_SHORT)
                .show());
    }

    /**
     * 监听事件
     * 完成 连接服务器 完成密码修改
     * 返回 返回个人信息
     */
    private class addListener implements View.OnClickListener {
        @Override
        public void onClick (View v) {
            switch (v.getId()) {
                case R.id.bt_patient_change_complete:
                    myCorrectPas();
                        break;
                case R.id.ib_patient_change_return:
                    Sure();
                    break;
                default:
                    break;
            }
        }
    }

}
