package com.funnyseals.app.feature.patientPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.feature.patientMessage.PatientChatActivity;
import com.funnyseals.app.model.User;
import com.funnyseals.app.util.SocketUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 我的医生界面
 */
public class PatientMyDoctorActivity extends AppCompatActivity {

    private TextView tv_patient_doctor_name, tv_patient_doctor_hospital, tv_patient_doctor_post,tv_patient_doctor_age,tv_patient_doctor_sex;
    private Button        bt_patient_mydoctor_chat,ib_patient_doctor_return;
    private MyApplication myApplication;
    private String        myDoctor = "";
    private User          myUser;
    private String        name, hosptial, post,age,sex;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_my_doctor);
        myApplication = (MyApplication) getApplication();
        myUser = myApplication.getUser();
        init();
    }

    /**
     * 初始化控件
     */
    void init () {
        tv_patient_doctor_name = findViewById(R.id.tv_patient_doctor_name);
        tv_patient_doctor_hospital = findViewById(R.id.tv_patient_doctor_myhospital);
        tv_patient_doctor_post = findViewById(R.id.tv_patient_doctor_mypost);
        tv_patient_doctor_age=findViewById(R.id.tv_patient_doctor_myage);
        tv_patient_doctor_sex=findViewById(R.id.tv_patient_doctor_mysex);

        ib_patient_doctor_return = findViewById(R.id.ib_patient_doctor_return);
        ib_patient_doctor_return.setOnClickListener(new addListeners());
        bt_patient_mydoctor_chat = findViewById(R.id.bt_patient_mydoctor_chat);
        bt_patient_mydoctor_chat.setOnClickListener(new addListeners());
        myDoctor();

    }
    /**
     更新界面UI信息
     */
    void initText(){
        tv_patient_doctor_name.setText(name);
        tv_patient_doctor_post.setText(post);
        tv_patient_doctor_hospital.setText(hosptial);
        tv_patient_doctor_age.setText(age);
        tv_patient_doctor_sex.setText(sex);
    }

    /**
     * 服务器获取我的医生的信息
     */
    public void myDoctor () {
        Thread thread = new Thread(() -> {
            String send = "";
            Socket socket;
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ID", myUser.getMyDoctor());
                jsonObject.put("request_type", "6");
                jsonObject.put("user_type", "d");
                send = jsonObject.toString();
                socket = SocketUtil.getSendSocket();
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(send);
                out.close();
                Thread.sleep(1000);
                socket = SocketUtil.setPort(2023);
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String message = dataInputStream.readUTF();

                jsonObject = new JSONObject(message);
                myDoctor = jsonObject.getString("docID");
                name = jsonObject.get("docName").toString();
                hosptial = jsonObject.get("docCompany").toString();
                post = jsonObject.get("docTitle").toString();
                age=jsonObject.get("docAge").toString();
                sex=jsonObject.getString("docSex");
                initText();
                socket.close();
                Thread.interrupted();
            } catch (IOException | JSONException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        while (thread.isAlive()){

        }


    }

    /**
     * 监听事件
     * 返回，跳转个人中心
     * 发起聊天，跳转聊天界面
     */
    private class addListeners implements View.OnClickListener {

        @Override
        public void onClick (View v) {
            switch (v.getId()) {
                case R.id.ib_patient_doctor_return:
                    finish();
                    break;
                case R.id.bt_patient_mydoctor_chat:
                    Intent intent = new Intent(PatientMyDoctorActivity.this, PatientChatActivity
                            .class);
                    intent.putExtra("myDoctorAccount", myDoctor);
                    intent.putExtra("myDoctorName", name);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

}
