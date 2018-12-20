package com.funnyseals.app.feature.patientPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.model.User;
import com.funnyseals.app.util.SocketUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 患者端
 * 个人信息界面
 */
public class PatientMyInfoModifyActivity extends AppCompatActivity {

    private EditText ed_patient_modify_myname, ed_patient_modify_mysex, ed_patient_modify_myage,
            ed_patient_modify_location;
    private TextView tv_patient_info_account, tv_patient_modify_mysettlingtime;
    private Button bt_patient_modify_complete;
    private String et1, et2, et3, et4;
    private ImageButton ib_patient_modify_return, ib_patient_modify_password,
            ib_patient_modify_advice;
    private User          myUser;
    private MyApplication myApplication;

    /**
     * 调用本地
     * 工具类USER
     */
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_my_info_modify);
        myApplication = (MyApplication) getApplication();
        myUser = myApplication.getUser();
        init();

    }

    /**
     * 初始化控件
     */
    public void init () {
        ed_patient_modify_myname = findViewById(R.id.ed_patient_modify_myname);
        ed_patient_modify_mysex = findViewById(R.id.ed_patient_modify_mysex);
        ed_patient_modify_myage = findViewById(R.id.ed_patient_modify_myage);
        ed_patient_modify_location = findViewById(R.id.ed_patient_modify_location);
        tv_patient_info_account = findViewById(R.id.tv_patient_info_account);
        tv_patient_modify_mysettlingtime = findViewById(R.id.tv_patient_modify_mysettlingtime);

        ed_patient_modify_myname.setText(myUser.getName());
        ed_patient_modify_myage.setText(String.valueOf(myUser.getAge()));
        ed_patient_modify_mysex.setText(myUser.getSex());
        ed_patient_modify_location.setText(myUser.getAddress());
        tv_patient_info_account.setText(myUser.getAccount());
        tv_patient_modify_mysettlingtime.setText(myUser.getRegisterTime());

        bt_patient_modify_complete = findViewById(R.id.bt_patient_modify_complete);
        bt_patient_modify_complete.setOnClickListener(new addListeners());
        ib_patient_modify_return = findViewById(R.id.ib_patient_modify_return);
        ib_patient_modify_return.setOnClickListener(new addListeners());
        ib_patient_modify_password = findViewById(R.id.ib_patient_modify_change_password);
        ib_patient_modify_password.setOnClickListener(new addListeners());
        ib_patient_modify_advice = findViewById(R.id.ib_patient_modify_advice);
        ib_patient_modify_advice.setOnClickListener(new addListeners());
    }

    /**
     * 返回按钮
     * 确认 返回个人中心
     * 取消 停留当前界面
     */
    public void Sure () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改未保存，确定退出？");
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
            finish();
        })
                .setNegativeButton("取消", (dialog, which) -> {
                    //取消对话框，返回界面
                    dialog.cancel();
                }).create().show();
        //只有点击按钮才行，点击空白无用
        builder.setCancelable(false);
    }

    public void showToast (final String msg) {
        runOnUiThread(() -> Toast.makeText(PatientMyInfoModifyActivity.this, msg, Toast.LENGTH_SHORT).show());
    }

    /**
     * 监听事件
     * 完成 连接服务器，更新界面信息
     * 返回 跳转个人中心
     * 修改密码 跳转修改密码界面
     * 医嘱  跳转医嘱病历界面
     */
    private class addListeners implements View.OnClickListener {
        @Override
        public void onClick (View v) {
            switch (v.getId()) {
                case R.id.bt_patient_modify_complete:
                    et1 = ed_patient_modify_myname.getText().toString().trim();
                    et2 = ed_patient_modify_myage.getText().toString().trim();
                    et3 = ed_patient_modify_mysex.getText().toString().trim();
                    et4 = ed_patient_modify_location.getText().toString().trim();
                    Thread thread = new Thread(() -> {
                        String send = "";
                        Socket socket;
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("pID", myApplication.getAccount());
                            jsonObject.put("pName", et1);
                            jsonObject.put("pAge", et2);
                            jsonObject.put("pSex", et3);
                            jsonObject.put("pAddress", et4);
                            jsonObject.put("request_type", "7");
                            jsonObject.put("user_type", "p");
                            send = jsonObject.toString();
                            socket = SocketUtil.getSendSocket();
                            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                            out.writeUTF(send);
                            out.close();

                            socket = SocketUtil.setPort(2019);
                            DataInputStream dataInputStream = new DataInputStream(socket
                                    .getInputStream());
                            String message = dataInputStream.readUTF();

                            jsonObject = new JSONObject(message);
                            switch (jsonObject.getString("update_result")) {
                                case "成功":
                                    myUser.setName(et1);
                                    myUser.setSex(et3);
                                    myUser.setAge(Integer.parseInt(et2));
                                    myUser.setAddress(et4);
                                    break;
                                case "失败":
                                    showToast("数据更新失败");
                                    break;

                            }
                            socket.close();
                            Thread.interrupted();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    });
                    thread.start();
                    while (thread.isAlive()) {

                    }
                    finish();
                    break;
                case R.id.ib_patient_modify_return:
                    Sure();
                    break;
                case R.id.ib_patient_modify_change_password:
                    Intent intent = new Intent(PatientMyInfoModifyActivity.this,
                            PatientPasswordActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ib_patient_modify_advice:
                    startActivity(new Intent(PatientMyInfoModifyActivity.this,
                            PatientDoctorAdviceActivity.class));
                    break;
                default:
                    break;
            }
        }
    }
}
