package com.funnyseals.app.feature.doctorPersonalCenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.feature.patientPersonalCenter.PatientMyDoctorActivity;
import com.funnyseals.app.feature.patientPersonalCenter.PatientPasswordActivity;
import com.funnyseals.app.feature.patientPersonalCenter.PatientPersonalCenterFragment;
import com.funnyseals.app.util.SocketUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DoctorSigningActivity extends AppCompatActivity {

    private EditText et_doctor_signing_phone;
    private ImageButton ib_doctor_signing_return;
    private Button bt_doctor_signing_complete,bt_doctor_signing_delete;
    private String str1="";
    private MyApplication myApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_signing);
        init();

    }

    public void init(){
        et_doctor_signing_phone=findViewById(R.id.et_doctor_signing_phone);
        myApplication=(MyApplication)getApplication();

        bt_doctor_signing_complete=findViewById(R.id.bt_doctor_signing_complete);
        bt_doctor_signing_complete.setOnClickListener(new addListeners());
        bt_doctor_signing_delete=findViewById(R.id.bt_doctor_signing_delete);
        bt_doctor_signing_delete.setOnClickListener(new addListeners());
        ib_doctor_signing_return=findViewById(R.id.ib_doctor_signing_return);
        ib_doctor_signing_return.setOnClickListener(new addListeners());
    }
    public void showToast(final String msg) {
        runOnUiThread(() -> Toast.makeText(DoctorSigningActivity.this, msg, Toast.LENGTH_SHORT).show());
    }
    //监听
    @SuppressWarnings("deprecation")
    private class addListeners implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_doctor_signing_complete:
                    if (correctPhone()){
                        new Thread(()->{
                            String send="";
                            Socket socket;
                            try{
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("pID",et_doctor_signing_phone.getText().toString().trim());
                                jsonObject.put("docID",myApplication.getAccount());
                                jsonObject.put("request_type","10");
                                send = jsonObject.toString();
                                socket = SocketUtil.getSendSocket();
                                DataOutputStream out=new DataOutputStream(socket.getOutputStream());
                                out.writeUTF(send);
                                out.close();

                                socket = SocketUtil.setPort(2024);
                                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                                String message = dataInputStream.readUTF();

                                jsonObject = new JSONObject(message);
                                switch (jsonObject.getString("sign_result")){
                                    case "0":
                                        showToast("签约成功");
                                        finish();
                                        break;
                                    case "2":
                                        showToast("用户不存在");
                                        break;
                                    case "1":
                                        showToast("签约失败");
                                        break;
                                }
                                socket.close();
                            }catch (IOException | JSONException e){
                                e.printStackTrace();
                            }
                        }).start();
                    }
                    break;
                case R.id.ib_doctor_signing_return:
                    finish();
                    break;
                case R.id.bt_doctor_signing_delete:
                    new Thread(()->{
                        String send="";
                        Socket socket;
                        try{
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("pID",et_doctor_signing_phone.getText().toString().trim());
                            jsonObject.put("docID",myApplication.getAccount());
                            jsonObject.put("request_type","10");
                            jsonObject.put("sign_type","1");
                            send = jsonObject.toString();
                            socket = SocketUtil.getSendSocket();
                            DataOutputStream out=new DataOutputStream(socket.getOutputStream());
                            out.writeUTF(send);
                            out.close();

                            socket = SocketUtil.setPort(2025);
                            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                            String message = dataInputStream.readUTF();

                            jsonObject = new JSONObject(message);
                            switch (jsonObject.getString("sign_result")){
                                case "0":
                                    showToast("解约成功");
                                    finish();
                                    break;
                                case "2":
                                    showToast("没有该患者");
                                    break;
                                case "1":
                                    showToast("解约失败");
                                    break;
                            }
                            socket.close();
                        }catch (IOException | JSONException e){
                            e.printStackTrace();
                        }
                    }).start();
                default:
                    break;
            }

        }
    }
    @SuppressLint("ShowToast")
    public boolean correctPhone(){
        if (str1.trim().equals("")){
            Toast.makeText(DoctorSigningActivity.this,"输入不能为空",Toast.LENGTH_SHORT);
            return false;
        }
        else if(str1.length()!=11){
            Toast.makeText(DoctorSigningActivity.this,"手机号码错误",Toast.LENGTH_SHORT);
            return false;
        }
        else {
            Toast.makeText(DoctorSigningActivity.this,"签约成功",Toast.LENGTH_SHORT);
            return true;
        }
    }
}
