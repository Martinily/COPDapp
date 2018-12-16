package com.funnyseals.app.feature.doctorPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.patientPersonalCenter.PatientMyDoctorActivity;
import com.funnyseals.app.feature.patientPersonalCenter.PatientPersonalCenterFragment;

public class DoctorSigningActivity extends AppCompatActivity {

    private EditText et_doctor_signing_phone;
    private ImageButton ib_doctor_signing_return;
    private Button bt_doctor_signing_complete;
    private String str1="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_setting);
        init();

    }

    public void init(){

        et_doctor_signing_phone=findViewById(R.id.et_doctor_signing_phone);
        str1=et_doctor_signing_phone.getText().toString();


        bt_doctor_signing_complete=findViewById(R.id.bt_doctor_signing_complete);
        bt_doctor_signing_complete.setOnClickListener(new addListeners());
        ib_doctor_signing_return=findViewById(R.id.ib_doctor_signing_return);
        ib_doctor_signing_return.setOnClickListener(new addListeners());
    }
    //监听
    private class addListeners implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.bt_doctor_signing_complete:
                 if (correctPhone()==true){
                     finish();
                 }
                case R.id.ib_doctor_signing_return:
                    finish();
            }

        }
    }
    public boolean correctPhone(){
        if (str1.trim().equals("")){
            Toast.makeText(DoctorSigningActivity.this,"输入不能为空",Toast.LENGTH_SHORT);
            return false;

        }
        if(str1.length()!=11){
            Toast.makeText(DoctorSigningActivity.this,"手机号码错误",Toast.LENGTH_SHORT);
            return false;

        }
        else {
            Toast.makeText(DoctorSigningActivity.this,"签约成功",Toast.LENGTH_SHORT);
            return true;

        }
    }
}
