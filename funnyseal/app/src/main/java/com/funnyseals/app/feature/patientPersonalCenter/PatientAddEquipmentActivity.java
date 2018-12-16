package com.funnyseals.app.feature.patientPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.funnyseals.app.R;

public class PatientAddEquipmentActivity extends AppCompatActivity {
    private String m_str1,m_str2;
    private EditText ev_patient_add_name,ev_patient_add_state;
    private Button bt_patient_add_complete;
    private ImageButton ib_patient_add_return;
    private Intent intent1,intent2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_add_equipment);
        init();

    }
    public void Sure(){
        AlertDialog.Builder builder=new AlertDialog.Builder(PatientAddEquipmentActivity.this);
        builder.setMessage("修改未保存，确定退出？");
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
         finish();
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
            //取消对话框，返回界面
            dialog.cancel();
        });
        //只有点击按钮才行，点击空白无用
        builder.setCancelable(false);
        AlertDialog Builder1=builder.create();
        Builder1.show();

    }
    private void init(){
        //获取各个edittext值
        ev_patient_add_name=findViewById(R.id.ev_patient_add_name);
        ev_patient_add_state=findViewById(R.id.ev_patient_add_state);
        m_str1=ev_patient_add_name.getText().toString();
        m_str2=ev_patient_add_state.getText().toString();

        ib_patient_add_return=findViewById(R.id.ib_patient_add_return);
        ib_patient_add_return.setOnClickListener(new addListeners());
        bt_patient_add_complete=findViewById(R.id. bt_patient_add_complete);
        bt_patient_add_complete.setOnClickListener(new addListeners());
    }
    //监听
    private class addListeners implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ib_patient_add_return:
                    Sure();
                    break;
                case  R.id. bt_patient_add_complete:
                    intent2 = new Intent(PatientAddEquipmentActivity.this,PatientMyEquipmentActivity.class);
                    startActivity(intent2);
                    break;
                default:
                    break;
            }
        }
    }
}
