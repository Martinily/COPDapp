package com.funnyseals.app.feature.doctorPersonalCenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.feature.bottomtab.DoctorBottomActivity;
import com.funnyseals.app.feature.patientPersonalCenter.PatientAddEquipmentActivity;
import com.funnyseals.app.util.SocketUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 医生端
 * 签约界面 activity
 */
public class DoctorSigningActivity extends AppCompatActivity {

    private EditText    et_doctor_signing_phone;
    private Button      bt_doctor_signing_complete,ib_doctor_signing_return, bt_doctor_signing_delete;
    private String        str1   = "";
    private MyApplication myApplication;
    private boolean       isType = true;
    private DoctorBottomActivity bottomActivity;
    private String         result="",result_delete="";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_signing);
        myApplication = (MyApplication) getApplication();
        bottomActivity = (DoctorBottomActivity) ((MyApplication)getApplication()).getBottom();
        init();


    }
    public void sureSigning () {
        AlertDialog.Builder builder = new AlertDialog.Builder(DoctorSigningActivity.this);
        builder.setMessage("确定签约？");
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
            Signing();
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
            //取消对话框，返回界面
            dialog.cancel();
        });
        //只有点击按钮才行，点击空白无用
        builder.setCancelable(false);
        AlertDialog Builder1 = builder.create();
        Builder1.show();

    }
    public void sureDelte () {
        AlertDialog.Builder builder = new AlertDialog.Builder(DoctorSigningActivity.this);
        builder.setMessage("确定解约？");
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
            Rescission();

        });
        builder.setNegativeButton("取消", (dialog, which) -> {
            //取消对话框，返回界面
            dialog.cancel();
        });
        //只有点击按钮才行，点击空白无用
        builder.setCancelable(false);
        AlertDialog Builder1 = builder.create();
        Builder1.show();

    }

    /**
     * 签约
     */
    public void Signing(){
        if (isType) {
            Thread thread=new Thread(() -> {
                String send = "";
                Socket socket;
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("pID", et_doctor_signing_phone.getText().toString
                            ().trim());
                    jsonObject.put("docID", myApplication.getAccount());
                    jsonObject.put("request_type", "10");
                    jsonObject.put("sign_type", "0");
                    send = jsonObject.toString();
                    socket = SocketUtil.getSendSocket();
                    DataOutputStream out = new DataOutputStream(socket
                            .getOutputStream());
                    out.writeUTF(send);
                    out.close();

                    socket = SocketUtil.setPort(2024);
                    DataInputStream dataInputStream = new DataInputStream(socket
                            .getInputStream());
                    String message = dataInputStream.readUTF();

                    JSONObject jsonObject1 = new JSONObject(message);

                    result=jsonObject1.getString("sign_result");

                    socket.close();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                Thread.interrupted();
            });
            thread.start();
            while (thread.isAlive()){

            }
            switch (result) {
                case "0":
                    Toast.makeText(DoctorSigningActivity.this, "签约成功", Toast
                            .LENGTH_LONG).show();
                    finish();
                    break;
                case "2":
                    Toast.makeText(DoctorSigningActivity.this, "用户不存在", Toast
                            .LENGTH_LONG).show();
                    break;
                case "1":
                    Toast.makeText(DoctorSigningActivity.this, "当前患者已签约", Toast
                            .LENGTH_LONG).show();
                    break;
            }

        }
    }

    /**
     * 解约
     */
    public void Rescission(){
        Thread thread = new Thread(() -> {
            String send = "";
            Socket socket;
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("pID", et_doctor_signing_phone.getText().toString()
                        .trim());
                jsonObject.put("docID", myApplication.getAccount());
                jsonObject.put("request_type", "10");
                jsonObject.put("sign_type", "1");
                send = jsonObject.toString();
                socket = SocketUtil.getSendSocket();
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(send);
                out.close();

                Thread.sleep(1000);

                socket = SocketUtil.setPort(2025);
                DataInputStream dataInputStream = new DataInputStream(socket
                        .getInputStream());
                String message = dataInputStream.readUTF();

                JSONObject jsonObject1 = new JSONObject(message);
                result_delete = jsonObject1.getString("sign_result");

                socket.close();
            } catch (IOException | JSONException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        while (thread.isAlive()){

        }
        switch (result_delete) {
            case "0":
                Toast.makeText(DoctorSigningActivity.this, "解约成功", Toast
                        .LENGTH_LONG).show();
                finish();
                break;
            case "2":
                Toast.makeText(DoctorSigningActivity.this, "没有该患者", Toast
                        .LENGTH_LONG).show();
                break;
            case "1":
                Toast.makeText(DoctorSigningActivity.this, "当前网络不稳定，请换个姿势试试~", Toast
                        .LENGTH_LONG).show();
                break;
        }

    }
    /**
     * 初始化控件
     */
    public void init () {
        et_doctor_signing_phone = findViewById(R.id.et_doctor_signing_phone);


        bt_doctor_signing_complete = findViewById(R.id.bt_doctor_signing_complete);
        bt_doctor_signing_complete.setOnClickListener(new addListeners());
        bt_doctor_signing_delete = findViewById(R.id.bt_doctor_signing_delete);
        bt_doctor_signing_delete.setOnClickListener(new addListeners());
        ib_doctor_signing_return = findViewById(R.id.ib_doctor_signing_return);
        ib_doctor_signing_return.setOnClickListener(new addListeners());
    }

    /**
     * 号码判定
     * 不能为空
     * 11位
     */
    @SuppressLint("ShowToast")
    public void correctPhone () {
        if (str1.trim().equals("")) {
            Toast.makeText(DoctorSigningActivity.this, "输入不能为空", Toast.LENGTH_SHORT);

        } else if (str1.length() != 11) {
            Toast.makeText(DoctorSigningActivity.this, "手机号码错误", Toast.LENGTH_SHORT);

        } else {
            Toast.makeText(DoctorSigningActivity.this, "签约成功", Toast.LENGTH_SHORT);
            isType = true;
        }
    }

    /**
     * 监听
     * 连接服务器，完成签约，跳转个人中心
     * 跳转个人中心
     * 连接服务器，删除签约人，跳转个人中心
     */
    @SuppressWarnings("deprecation")
    private class addListeners implements View.OnClickListener {
        @Override
        public void onClick (View v) {
            switch (v.getId()) {
                case R.id.bt_doctor_signing_complete:
                    correctPhone ();
                    sureSigning();
                    break;
                case R.id.ib_doctor_signing_return:
                    finish();
                    break;
                case R.id.bt_doctor_signing_delete:
                    correctPhone ();
                    sureDelte();
                    break;
                default:
                    break;
            }
        }
    }

}
