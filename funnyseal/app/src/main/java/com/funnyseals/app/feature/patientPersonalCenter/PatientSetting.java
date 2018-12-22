package com.funnyseals.app.feature.patientPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.funnyseals.app.LoginActivity;
import com.funnyseals.app.R;

/**
 * 患者端
 * 设置界面
 */
public class PatientSetting extends AppCompatActivity {

    private TextView    tv_patient_setting_finish;
    private ImageButton ib_patient_setting_return;

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_setting);
    }

    /**
     * 初始化控件
     */
    public void init () {
        tv_patient_setting_finish = findViewById(R.id.tv_patient_setting_finish);
        ib_patient_setting_return = findViewById(R.id.ib_patient_setting_return);
        tv_patient_setting_finish.setOnClickListener(new addListeners());
        ib_patient_setting_return.setOnClickListener(new addListeners());
    }

    /**
     * 监听事件
     */
    private class addListeners implements View.OnClickListener {
        @Override
        public void onClick (View v) {
            switch (v.getId()) {
                case R.id.ib_patient_setting_return:
                    finish();
                    break;
                case R.id.tv_patient_setting_finish:
                    startActivity(new Intent(PatientSetting.this, LoginActivity.class));
                    break;
                default:
                    break;
            }
        }
    }
}
