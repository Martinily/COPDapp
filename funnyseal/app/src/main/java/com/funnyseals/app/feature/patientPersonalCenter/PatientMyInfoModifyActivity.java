package com.funnyseals.app.feature.patientPersonalCenter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.funnyseals.app.R;

public class PatientMyInfoModifyActivity extends AppCompatActivity {

    private Activity mactivity;
    private Context mcontext;
    private EditText  ed_patient_modify_myname,ed_patient_modify_mysex,ed_patient_modify_myage,ed_patient_modify_location;
    private Button bt_patient_modify_complete;
    private String et1,et2,et3,et4;
    private ImageButton ib_patient_modify_return,ib_patient_modify_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_my_info_modify);
        init();

    }
    public void init(){
        ed_patient_modify_myname=findViewById(R.id.ed_patient_modify_myname);
        et1=ed_patient_modify_myname.getText().toString();
        ed_patient_modify_mysex=findViewById(R.id.ed_patient_modify_mysex);
        et2=ed_patient_modify_mysex.getText().toString();
        ed_patient_modify_myage=findViewById(R.id.ed_patient_modify_myage);
        et3=ed_patient_modify_myage.getText().toString();
        ed_patient_modify_location=findViewById(R.id.ed_patient_modify_location);
        et4=ed_patient_modify_location.getText().toString();

        bt_patient_modify_complete=findViewById(R.id.bt_patient_modify_complete);
        bt_patient_modify_complete.setOnClickListener(new addListeners());
        ib_patient_modify_return=findViewById(R.id.ib_patient_modify_return);
        ib_patient_modify_return.setOnClickListener(new addListeners());
        ib_patient_modify_password=findViewById(R.id.ib_patient_modify_change_password);
        ib_patient_modify_password.setOnClickListener(new addListeners());
    }
    //弹出框设置
    public void Sure(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("修改未保存，确定退出？");
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
            finish();
        })
        .setNegativeButton("取消", (dialog, which) -> {
           //取消对话框，返回界面
            dialog.cancel();
        }).create().show();
        //只有点击按钮才行，点击空白无用
        builder.setCancelable(false);
    }

    //监听
    private  class addListeners  implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_patient_modify_complete:
                    ed_patient_modify_myage.setText(et3.toCharArray(),0,et3.length());
                    ed_patient_modify_myname.setText(et1.toCharArray(),0,et1.length());
                    ed_patient_modify_mysex.setText(et2.toCharArray(),0,et2.length());
                    ed_patient_modify_location.setText(et4.toCharArray(),0,et4.length());
                    finish();
                    break;
                case  R.id.ib_patient_modify_return:
                    Sure();
                    break;
                case R.id.ib_patient_modify_change_password:
                    Intent intent=new Intent(PatientMyInfoModifyActivity.this,PatientPasswordActivity.class);
                    startActivity(intent);
                default:
                    break;
            }
        }
    }
}
