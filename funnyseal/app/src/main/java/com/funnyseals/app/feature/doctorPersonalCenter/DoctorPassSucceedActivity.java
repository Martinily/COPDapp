package com.funnyseals.app.feature.doctorPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.funnyseals.app.R;

public class DoctorPassSucceedActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_pass_succeed);
        run();
    }
    //自动跳转
    public void run(){
        Intent intent=new Intent(DoctorPassSucceedActivity.this,DoctorMyInfoActivity.class);
        finish();
    }
}
