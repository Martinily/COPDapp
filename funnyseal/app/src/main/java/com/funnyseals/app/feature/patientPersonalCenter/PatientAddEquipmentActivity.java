package com.funnyseals.app.feature.patientPersonalCenter;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.funnyseals.app.LoginActivity;
import com.funnyseals.app.R;
import com.funnyseals.app.util.SocketUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PatientAddEquipmentActivity extends AppCompatActivity {

    private EditText ev_patient_add_name,ev_patient_add_state;
    private Button bt_patient_add_complete;
    private ImageButton ib_patient_add_return;

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


        ib_patient_add_return=findViewById(R.id.ib_patient_add_return);
        ib_patient_add_return.setOnClickListener(new addListeners());
        bt_patient_add_complete=findViewById(R.id. bt_patient_add_complete);
        bt_patient_add_complete.setOnClickListener(new addListeners());
    }
    //监听
    private class addListeners implements View.OnClickListener{

        @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ib_patient_add_return:
                    Sure();
                    break;
                case  R.id. bt_patient_add_complete:
                   addEquipment();
                    break;
                default:
                    break;
            }
        }
    }
    //连接服务器
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void addEquipment(){
        if (getName().isEmpty()||getState().isEmpty()){
           showToast("设备名称或者状态不能为空");
        }
        else {
            new Thread(()->{
                String send="";
                Socket socket;
                try{
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("pID","xxxxxx");
                    jsonObject.put("request_type","1");
                    jsonObject.put("equipment_name", getName());
                    jsonObject.put("equipment_state", getState());
                    send = jsonObject.toString();
                    socket = SocketUtil.getSendSocket();
                    DataOutputStream out=new DataOutputStream(socket.getOutputStream());
                    out.writeUTF(send);
                    out.close();

                    socket = SocketUtil.getGetSocket();
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    String message = dataInputStream.readUTF();

                    jsonObject = new JSONObject(message);
                    switch (jsonObject.getString("xx")){
                        case "true":
                            showToast("添加成功");
                            finish();
                            break;
                        case "false":
                            showToast("添加失败");
                            break;
                    }
                    socket.close();
                }catch (IOException | JSONException e){
                    e.printStackTrace();
                }
            }).start();
        }
    }
    private String getName(){
        return ev_patient_add_name.getText().toString().trim();
    }
    private String getState(){
        return ev_patient_add_state.getText().toString().trim();
    }

    public void showToast(final String msg) {
        runOnUiThread(() -> Toast.makeText(PatientAddEquipmentActivity.this, msg, Toast.LENGTH_SHORT).show());
    }
}
