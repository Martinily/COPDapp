package com.funnyseals.app.feature.patientPersonalCenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.funnyseals.app.R;

import java.util.Timer;
import java.util.TimerTask;

public class PatientPasswordActivity extends AppCompatActivity {
    private Button bt_patient_change_complete;
    private ImageButton ib_patient_change_return;
    private Intent intent1;
    private EditText et_patient_change_oldpassword,et_patient_change_newpassword,et_patient_change_againpassword;
    private String oldpassword,newpassword,againpassword;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_password);
        init();
        myCorrectPas();
    }
    //密码判断
    public boolean myCorrectPas(){

        if (oldpassword.equals("")||newpassword.equals("")||againpassword.equals(""))
        {
            Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isLetterOrDigit(newpassword)){
            Toast.makeText(this,"密码不能包含特殊字符",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(newpassword.length()<6 || newpassword.length()>16){
            Toast.makeText(this,"密码长度应为6-16位",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isSame(newpassword, againpassword)){
            Toast.makeText(this,"两次密码不一致，请重新输入", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }



    //初始化
    private void init(){
        //获取各个edittext值
        et_patient_change_oldpassword=findViewById(R.id.et_patient_change_oldpassword);
        et_patient_change_newpassword=findViewById(R.id.et_patient_change_newpassword);
        et_patient_change_againpassword=findViewById(R.id.et_patient_change_againpassword);
        oldpassword=et_patient_change_oldpassword.getText().toString().trim();
        newpassword=et_patient_change_newpassword.getText().toString().trim();
        againpassword=et_patient_change_againpassword.getText().toString().trim();

        bt_patient_change_complete=findViewById(R.id.bt_patient_change_complete);
        ib_patient_change_return=findViewById(R.id.ib_patient_change_return);
        bt_patient_change_complete.setOnClickListener(new addListener());
        ib_patient_change_return.setOnClickListener(new addListener());
    }

    //判断密码不能有特殊字符，只能是英文或者数字
    public static boolean isLetterOrDigit(String str){
        boolean isLetterOrDigit = false;
        for (int i=0;i<str.length();i++)
            if (Character.isLetterOrDigit(str.charAt(i))){
                isLetterOrDigit=true;
            }
        String regex=  "^([A-Z]|[a-z]|[0-9])$";
        return  (isLetterOrDigit && str.matches(regex));
    }
    //判断两次密码一致
    public boolean isSame(String str1,String str2){
        boolean isSame = false;
        if(str1.equals(str2)){
            isSame=true;
        }
        return isSame;
    }
    //提示
    public void Sure(){
        AlertDialog.Builder builder=new AlertDialog.Builder(PatientPasswordActivity.this);
        builder.setMessage("密码未修改，确定退出？");
        builder.setPositiveButton("确定", (dialog, which) -> {
         //   dialog.dismiss();
            finish();
        })
        .setNegativeButton("取消", (dialog, which) -> {
            //取消对话框，返回界面
            dialog.cancel();
        }).create().show();
        //只有点击按钮才行，点击空白无用
        builder.setCancelable(false);
    }
    //延时
    public void runTime(){
        Timer timer=new Timer();
        TimerTask  timerTask=new TimerTask() {
            @Override
            public void run() {
                Toast.makeText(PatientPasswordActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                finish();

            }
        };
        timer.schedule(timerTask,2000);
    }
    //监听
   private class addListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_patient_change_complete:
                    if (myCorrectPas()==true){
                        runTime();
                    }
                    break;
                case R.id.ib_patient_change_return:
                    Sure();

                    break;
                default:
                    break;
            }
        }
    }

}
