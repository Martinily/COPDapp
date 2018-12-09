package com.funnyseals.app.feature.patientPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.doctorPersonalCenter.DoctorMyInfoActivity;
import com.funnyseals.app.feature.doctorPersonalCenter.DoctorPassSucceedActivity;
import com.funnyseals.app.feature.doctorPersonalCenter.DoctorPersonalCenterFragment;

public class PatientPassSucceedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_pass_succeed);
       toActivity();
    }

    //自动跳转
    private void toActivity(){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent();
                intent.setClass(PatientPassSucceedActivity.this,DoctorPersonalCenterFragment.class);
                startActivity(intent);
                finish();
            }
        };
        new Handler().postDelayed(runnable,800);//1秒后关闭
    }

}
