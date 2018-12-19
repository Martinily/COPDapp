package com.funnyseals.app.feature.patientPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.model.User;
/**
*医嘱、病历界面
 */
public class PatientDoctorAdviceActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_patient_advice_myAdvice,tv_patient_advice_myrecord;
    private ImageButton ib_patient_advice_return;
    private MyApplication myApplication;
    private User myUser;

    /**
     * 从本地获取信息，调用工具类
     * USER
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_doctor_advice);
        myApplication=(MyApplication) getApplication();
        myUser=myApplication.getUser();
        init();
    }
    /**
    *初始化控件
     */
    public void init(){
        tv_patient_advice_myAdvice=findViewById(R.id.tv_patient_advice_myAdvice);
        tv_patient_advice_myrecord=findViewById(R.id.tv_patient_advice_myrecord);
        tv_patient_advice_myAdvice.setText(myUser.getMedicalOrder());
        tv_patient_advice_myrecord.setText(myUser.getMedicalHistory());
        ib_patient_advice_return=findViewById(R.id.ib_patient_advice_return);
        ib_patient_advice_return.setOnClickListener(this);
        doctorAdvice();
    }
    /**
    *如果接收为null
     * 改变显示
     */
    public void doctorAdvice(){
        if (tv_patient_advice_myrecord.getText().toString().equals("null")){
            tv_patient_advice_myrecord.setText("当前并无病历");
        }
        if (tv_patient_advice_myAdvice.getText().toString().equals("null")){
            tv_patient_advice_myAdvice.setText("当前并无医嘱");
        }
    }
    /**
    *监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_patient_advice_return:
                finish();
                break;
        }
    }
}
