package com.funnyseals.app.feature.patientPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.funnyseals.app.LoginActivity;
import com.funnyseals.app.R;

/**
 * 患者端
 * 设置界面
 */
public class PatientSetting extends AppCompatActivity {

    private Button bt_patient_setting_finish;
    private ImageButton ib_patient_setting_return;

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_setting);
        init();
    }

    /**
     * 初始化控件
     */
    public void init () {
        bt_patient_setting_finish = findViewById(R.id.bt_patient_setting_finish);
        ib_patient_setting_return = findViewById(R.id.ib_patient_setting_return);
        bt_patient_setting_finish.setOnClickListener(new addListeners());
        ib_patient_setting_return.setOnClickListener(new addListeners());
    }
    public void Sure () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确定登出？");
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
            startActivity(new Intent(PatientSetting.this, LoginActivity.class));
        })
                .setNegativeButton("取消", (dialog, which) -> {
                    //取消对话框，返回界面
                    dialog.cancel();
                }).create().show();
        //只有点击按钮才行，点击空白无用
        builder.setCancelable(false);
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
                case R.id.bt_patient_setting_finish:
                    Sure();
                    break;
                default:
                    break;
            }
        }
    }
}
