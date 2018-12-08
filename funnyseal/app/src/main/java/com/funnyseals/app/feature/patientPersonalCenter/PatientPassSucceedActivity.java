package com.funnyseals.app.feature.patientPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.doctorPersonalCenter.DoctorMyInfoActivity;

public class PatientPassSucceedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_pass_succeed);
        run();
    }

    //一定时间后跳转回去
    public void run(){
        Intent intent=new Intent(PatientPassSucceedActivity.this,DoctorMyInfoActivity.class);
        finish();
    }

}
