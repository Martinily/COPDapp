package com.funnyseals.app.feature.doctorPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.funnyseals.app.R;

public class DoctorMyInfoModifyActivity extends AppCompatActivity {

    private  String et1="",et2="",et3="",et4="",et5="",et6="";
    private EditText et_doctor_modify_myname,et_doctor_modify_mysex,et_doctor_modify_myage,et_doctor_modify_mylocation,et_doctor_modify_myhospital,et_doctor_modify_mypost;
    private Button bt_doctor_modify_complete;
    private ImageButton ib_doctor_modify_return,ib_doctor_modify_changepassword;
    private Intent intent1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_my_info_modify);
        init();
    }

    private void init(){
        //获取各个edittext值，并改变界面的值
        et_doctor_modify_myage=findViewById(R.id.et_doctor_modify_myage);
        et1=et_doctor_modify_myage.getText().toString();


        et_doctor_modify_myhospital=findViewById(R.id.et_doctor_modify_myhospital);
        et2=et_doctor_modify_myhospital.getText().toString();


        et_doctor_modify_myname=findViewById(R.id.et_doctor_modify_myname);
        et3=et_doctor_modify_myname.getText().toString();


        et_doctor_modify_mysex=findViewById(R.id.et_doctor_modify_mysex);
        et4=et_doctor_modify_myname.getText().toString();


        et_doctor_modify_mylocation=findViewById(R.id.et_doctor_modify_mylocation);
        et5=et_doctor_modify_mylocation.getText().toString();


        et_doctor_modify_mypost=findViewById(R.id.et_doctor_modify_mypost);
        et6=et_doctor_modify_mypost.getText().toString();


        ib_doctor_modify_return=findViewById(R.id. ib_doctor_modify_return);
        ib_doctor_modify_return.setOnClickListener(new addListeners());
        bt_doctor_modify_complete=findViewById(R.id.bt_doctor_modify_complete);
        bt_doctor_modify_complete.setOnClickListener(new addListeners());
        ib_doctor_modify_changepassword=findViewById(R.id.ib_doctor_modify_changepassword);
        ib_doctor_modify_changepassword.setOnClickListener(new addListeners());
    }
    //弹出框设置
    public void Sure(){
        AlertDialog.Builder builder=new AlertDialog.Builder(DoctorMyInfoModifyActivity.this);
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

    //监听
    private class addListeners implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ib_doctor_modify_return:
                    Sure();
                    break;
                case  R.id.bt_doctor_modify_complete:
                    et_doctor_modify_myage.setText(et1.toCharArray(),0,et1.length());
                    et_doctor_modify_myhospital.setText(et2.toCharArray(),0,et2.length());
                    et_doctor_modify_myname.setText(et3.toCharArray(),0,et3.length());
                    et_doctor_modify_mysex.setText(et4.toCharArray(),0,et4.length());
                    et_doctor_modify_mylocation.setText(et5.toCharArray(),0,et5.length());
                    et_doctor_modify_mypost.setText(et6.toCharArray(),0,et6.length());
                    finish();
                    break;
                case R.id.ib_doctor_modify_changepassword:
                    intent1=new Intent(DoctorMyInfoModifyActivity.this,DoctorPasswordActivity.class);
                    startActivity(intent1);
                    break;
                default:
                    break;
            }
        }
    }

}
