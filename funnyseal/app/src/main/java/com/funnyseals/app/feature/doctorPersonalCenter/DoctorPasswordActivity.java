package com.funnyseals.app.feature.doctorPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.funnyseals.app.R;

public class DoctorPasswordActivity extends AppCompatActivity {

    private EditText et_doctor_change_oldpassword,et_doctor_change_newpassword,et_doctor_change_againpassword;
    private Button bt_doctor_change_complete;
    private ImageButton ib_doctor_change_return;
    private Intent intent1,intent2;
    private String  newpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_doctor_password);
            init();
           if( isLetterOrDigit(newpassword)==false){
               Toast.makeText(this,"密码不能包含特殊字符",Toast.LENGTH_SHORT).show();
           }
           if(newpassword.length()<6 || newpassword.length()>16){
               Toast.makeText(this,"密码长度应为6-16位",Toast.LENGTH_SHORT).show();
           }

    }
    //判断密码不能有特殊字符，只能是英文或者数字
    public static boolean isLetterOrDigit(String str){
            boolean isLetterOrDigit = false;
            for (int i=0;i<str.length();i++)
                   if (Character.isLetterOrDigit(str.charAt(i))){
                    isLetterOrDigit=true;
            }
            String regex="^[a-zA-Z0-9]+$";
            boolean isRight=isLetterOrDigit && str.matches(regex);
            return  isRight;
    }
    private void init(){
        //获取各个edittext值
        et_doctor_change_oldpassword=findViewById(R.id.et_doctor_change_oldpassword);
        et_doctor_change_newpassword=findViewById(R.id.et_doctor_change_newpassword);
        newpassword=et_doctor_change_newpassword.toString();
        et_doctor_change_againpassword=findViewById(R.id.et_doctor_change_againpassword);

        ib_doctor_change_return=(ImageButton)findViewById(R.id. ib_doctor_change_return);
        ib_doctor_change_return.setOnClickListener(new addListeners());
        bt_doctor_change_complete=(Button)findViewById(R.id.bt_doctor_change_complete);
        bt_doctor_change_complete.setOnClickListener(new addListeners());
    }
    //监听
    private class addListeners implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ib_doctor_change_return:
                    intent1 = new Intent(DoctorPasswordActivity.this,DoctorMyInfoActivity.class);
                    startActivity( intent1);
                    break;
                case  R.id.bt_doctor_change_complete:
                   intent2= new Intent(DoctorPasswordActivity.this,DoctorPassSucceedActivity.class);
                    startActivity( intent2);
                    break;
                default:
                    break;
            }
        }
    }
}
