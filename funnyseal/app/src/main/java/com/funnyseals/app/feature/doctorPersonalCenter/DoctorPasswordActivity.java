package com.funnyseals.app.feature.doctorPersonalCenter;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.feature.patientPersonalCenter.PatientPasswordActivity;
import com.funnyseals.app.util.SocketUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class DoctorPasswordActivity extends AppCompatActivity {
    private EditText et_doctor_change_oldpassword,et_doctor_change_newpassword,et_doctor_change_againpassword;
    private Button bt_doctor_change_complete;
    private ImageButton ib_doctor_change_return;
    private MyApplication myApplication;
    private boolean type=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_doctor_password);
            init();

    }
    //密码判断
    public boolean myCorrectPas(String oldpassword,String newpassword,String againpassword){

        if (oldpassword.equals("")||newpassword.equals("")||againpassword.equals(""))
        {
            Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isLetterOrDigit(newpassword)){
            Toast.makeText(this,"密码不能包含特殊字符",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(newpassword.length()<6 || newpassword.length()>20){
            Toast.makeText(this,"密码长度应为6-20位",Toast.LENGTH_SHORT).show();
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
    //判断两次密码一致
    public boolean isSame(String str1,String str2){
            boolean isSame = false;
            if(str1.equals(str2)){
                isSame=true;
            }
            return isSame;
    }



    private void init(){
        //获取各个edittext值
        et_doctor_change_oldpassword=findViewById(R.id.et_doctor_change_oldpassword);
        et_doctor_change_newpassword=findViewById(R.id.et_doctor_change_newpassword);
        et_doctor_change_againpassword=findViewById(R.id.et_doctor_change_againpassword);
        myApplication=(MyApplication)getApplication();

        ib_doctor_change_return=findViewById(R.id. ib_doctor_change_return);
        ib_doctor_change_return.setOnClickListener(new addListeners());
        bt_doctor_change_complete=findViewById(R.id.bt_doctor_change_complete);
        bt_doctor_change_complete.setOnClickListener(new addListeners());

    }
    public void Sure(){
        AlertDialog.Builder builder=new AlertDialog.Builder(DoctorPasswordActivity.this);
        builder.setMessage("密码未修改，确定退出？");
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
           finish();
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
            //取消对话框，返回界面
            dialog.cancel();
        }).create().show();
        //只有点击按钮才行，点击空白无用
        builder.setCancelable(false);


    }
    public String getOldpassword(){
        return et_doctor_change_oldpassword.getText().toString().trim();
    }
    public String getNewpassword(){
        return et_doctor_change_newpassword.getText().toString().trim();
    }
    public String getAgainpassword(){
        return et_doctor_change_againpassword.getText().toString().trim();
    }
    public void showToast(final String msg) {
        runOnUiThread(() -> Toast.makeText(DoctorPasswordActivity.this, msg, Toast.LENGTH_SHORT).show());
    }
    //监听
    private class addListeners implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ib_doctor_change_return:
                    Sure();
                    break;
                case  R.id.bt_doctor_change_complete:
                    if (myCorrectPas(getOldpassword(),getNewpassword(),getAgainpassword())){
                        new Thread(()->{
                            String send="";
                            Socket socket;
                            try{
                                JSONObject jsonObject = new JSONObject();

                                jsonObject.put("request_type","8");
                                jsonObject.put("modify_type","update");
                                jsonObject.put("ID",myApplication.getAccount());
                                jsonObject.put("oldPassword", getOldpassword());
                                jsonObject.put("newPassword",getNewpassword());
                                send = jsonObject.toString();
                                socket = SocketUtil.getSendSocket();
                                DataOutputStream out=new DataOutputStream(socket.getOutputStream());
                                out.writeUTF(send);
                                out.close();

                                socket = SocketUtil.getGetSocket();
                                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                                String message = dataInputStream.readUTF();

                                jsonObject = new JSONObject(message);
                                switch (jsonObject.getString("password_result")){
                                    case "0":
                                        type=true;
                                        showToast("修改密码成功");

                                        break;
                                    case "1":
                                        showToast("用户名错误");
                                        break;
                                    case "2":
                                        showToast("密码错误");
                                        break;
                                    case "3":
                                        showToast("修改失败");
                                        break;
                                }
                                socket.close();
                            }catch (IOException | JSONException e){
                                e.printStackTrace();
                            }
                        }).start();
                    }
                    if (type=true){
                        finish();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
