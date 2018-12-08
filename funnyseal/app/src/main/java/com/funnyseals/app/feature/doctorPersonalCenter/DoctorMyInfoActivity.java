package com.funnyseals.app.feature.doctorPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnyseals.app.R;
import com.funnyseals.app.SignupActivity;
import com.funnyseals.app.feature.bottomtab.DoctorBottomActivity;
import com.funnyseals.app.feature.bottomtab.PatientBottomActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DoctorMyInfoActivity extends AppCompatActivity {

    private View view;
    private TextView tv_doctor_info_myname,tv_doctor_info_myage,tv_doctor_info_mycompany,tv_doctor_info_mylocation,tv_doctor_info_mysex,tv_doctor_info_myposition;
    private ImageButton ib_doctor_info_return;
    private Button bt_doctor_info_changeinfo;
    private ImageView iv_doctor_info_portrait;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_my_info);
        initUIComponents();
    }
    public void writeIntoDB() {
        new Thread(() -> {
            String send="";
            Socket socket;
            try{
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("request_type","2");
                jsonObject.put("ID",mEtAccount.getText().toString());
                jsonObject.put("Password",mEtPassword.getText().toString());
                jsonObject.put("register_type",mRbAccountTypeDoctor.isChecked()?"d":"p");
                send=jsonObject.toString();
                socket = SocketUtil.getSendSocket();
                DataOutputStream out=new DataOutputStream(socket.getOutputStream());
                out.writeUTF(send);
                out.close();

                socket = SocketUtil.getGetSocket();
                DataInputStream datainputstream=new DataInputStream(socket.getInputStream());
                String message=datainputstream.readUTF();

                jsonObject=new JSONObject(message);
                switch (jsonObject.getString("reg_result")) {
                    case "成功":
                        //EMClient.getInstance().createAccount(mEtAccount.getText().toString(), mEtPassword.getText().toString());
                        showToast("注册成功！");
                        //destorySendSMSHandler();
                        if(mRbAccountTypeDoctor.isChecked()){
                            startActivity(new Intent(SignupActivity.this,DoctorBottomActivity.class));
                            finish();
                        }else if(mRbAccountTypePatient.isChecked()){
                            startActivity(new Intent(SignupActivity.this,PatientBottomActivity.class));
                            finish();
                        }
                        break;
                    case "用户已存在":
                        showToast("该账号已被注册！");
                        break;
                }
                socket.close();
            } catch (IOException | JSONException e /*| HyphenateException e*/) {
                e.printStackTrace();
            }
        }).start();
    }

    //更新或者说初始化，其余的写死
    void   initUIComponents()
    {
        tv_doctor_info_mysex=view.findViewById(R.id. tv_doctor_info_mysex);
        tv_doctor_info_myname=view.findViewById(R.id.tv_doctor_info_myname);
        tv_doctor_info_myage=view.findViewById(R.id.tv_doctor_info_myage);
        tv_doctor_info_mycompany=view.findViewById(R.id.tv_doctor_info_mycompany);
        tv_doctor_info_mylocation=view.findViewById(R.id.tv_doctor_info_mylocation);
        tv_doctor_info_myposition=view.findViewById(R.id.tv_doctor_info_myposition);

        tv_doctor_info_mysex.setText();
        tv_doctor_info_myname.setText();
        tv_doctor_info_myage.setText();
        tv_doctor_info_mycompany.setText();
        tv_doctor_info_mylocation.setText();
        tv_doctor_info_myposition.setText();


    }
    //监听
    private void addListeners() {

    }

}
