package com.funnyseals.app.feature.patientPersonalCenter;


import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.funnyseals.app.R;
import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.util.SocketUtil;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
/**
 * 患者端
 * 添加设备界面 activity

 */
public class PatientAddEquipmentActivity extends AppCompatActivity {

    private Spinner sp_patient_add_name,sp_patient_add_state;
    private Button bt_patient_add_complete;
    private ImageButton ib_patient_add_return;
    private MyApplication myApplication;
    private String item_name,item_state;
    private boolean type=false;

    /**
     *适配器完成下拉控件
     * 两个下拉控件
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_add_equipment);

        myApplication = (MyApplication)getApplication();
        init();
        ArrayAdapter<CharSequence> adapter_name = ArrayAdapter.createFromResource(this,R.array.select_name,android.R.layout.simple_spinner_item);
        adapter_name.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_patient_add_name.setAdapter(adapter_name);
        sp_patient_add_name.setOnItemSelectedListener(new spinnerNameListener());
        ArrayAdapter<CharSequence>adapter_state = ArrayAdapter.createFromResource(this,R.array.select_state,android.R.layout.simple_spinner_item);
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_patient_add_state.setOnItemSelectedListener(new spinnerStateListener());
    }
    /**
     *第一个下拉控件的监听
     * 获取选择了下拉的那个选项
     */
    class spinnerNameListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            item_name  =parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    /**
     *第二个下拉控件的监听
     * 获取选择了下拉的那个选项
     */
    class spinnerStateListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            item_state = parent.getItemAtPosition(position).toString();
            if (item_state.equals("使用")){
                item_state="1";
            }
            else {
                item_state="0";
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    /**
     *返回按钮
     * 提示
     * 确认 返回我的设备
     * 取消 停留当前页面
     */
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
    /**
     *初始化控件
     */
    private void init(){
        sp_patient_add_name=findViewById(R.id.sp_patient_add_name);
        sp_patient_add_state=findViewById(R.id.sp_patient_add_state);

        ib_patient_add_return=findViewById(R.id.ib_patient_add_return);
        ib_patient_add_return.setOnClickListener(new addListeners());
        bt_patient_add_complete=findViewById(R.id. bt_patient_add_complete);
        bt_patient_add_complete.setOnClickListener(new addListeners());
    }
    /**
     *监听事件
     * 返回 我的设备
     * 添加完成
     */
    private class addListeners implements View.OnClickListener{
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
    /**
     *连接服务器
     * 完成添加设备操作
     */
    private void addEquipment(){
            new Thread(()->{
                String send="";
                Socket socket;
                try{
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("request_type","9");
                    jsonObject.put("device_type","add");
                    jsonObject.put("pID",myApplication.getAccount());
                    jsonObject.put("eName",item_name);
                    jsonObject.put("eState",item_state);

                    send = jsonObject.toString();
                    socket = SocketUtil.getSendSocket();
                    DataOutputStream out=new DataOutputStream(socket.getOutputStream());
                    out.writeUTF(send);
                    out.close();

                    socket = SocketUtil.setPort(2030);
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    String message = dataInputStream.readUTF();

                    jsonObject = new JSONObject(message);
                    switch (jsonObject.getString("device_result")){
                        case "true":
                            type=true;
                            Toast.makeText(PatientAddEquipmentActivity.this,"添加成功", Toast.LENGTH_LONG).show();
                            break;
                        case "false":
                            Toast.makeText(PatientAddEquipmentActivity.this,"添加失败", Toast.LENGTH_LONG).show();
                            break;
                    }
                    socket.close();
                }catch (IOException | JSONException e){
                    e.printStackTrace();
                }
            }).start();
                if (type){
                    finish();
                }
        }
}
