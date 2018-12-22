package com.funnyseals.app.feature.doctorPersonalCenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.funnyseals.app.LoginActivity;
import com.funnyseals.app.R;

/**
 * 医生端
 * 设置界面 activity
 */
@SuppressLint("Registered")
public class DoctorSettingActivity extends AppCompatActivity {
    private TextView tv_doctor_setting_finish;
    private ImageButton ib_doctor_setting_return;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_setting);
        init();
    }

    public void init () {
        tv_doctor_setting_finish=findViewById(R.id.tv_doctor_setting_finish);
        ib_doctor_setting_return=findViewById(R.id.ib_doctor_setting_return);
        tv_doctor_setting_finish.setOnClickListener(new addListeners());
        ib_doctor_setting_return.setOnClickListener(new addListeners());

    }
    /**
     * 监听事件
     */
    private class addListeners implements View.OnClickListener {
        @Override
        public void onClick (View v) {
            switch (v.getId()) {
                case R.id.ib_doctor_setting_return:
                    finish();
                    break;
                case R.id.tv_doctor_setting_finish:
                    startActivity(new Intent(DoctorSettingActivity.this, LoginActivity.class));
                    break;
                default:
                    break;
            }
        }
    }
}
