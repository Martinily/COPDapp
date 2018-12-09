package com.funnyseals.app.feature.doctorPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
     toActivity();
    }
    //自动跳转
    private void toActivity(){
       Runnable runnable=new Runnable() {
           @Override
           public void run() {
               Intent intent=new Intent();
               intent.setClass(DoctorPassSucceedActivity.this,DoctorPersonalCenterFragment.class);
               startActivity(intent);
               finish();
           }
       };
       new Handler().postDelayed(runnable,800);//1秒后关闭
    }
}
