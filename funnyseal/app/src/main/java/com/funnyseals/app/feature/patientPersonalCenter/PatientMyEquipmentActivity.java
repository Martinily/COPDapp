package com.funnyseals.app.feature.patientPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.funnyseals.app.R;
import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.model.AddEquipment;
import com.funnyseals.app.util.SocketUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
/**
 * 我的设备界面
 */
public class PatientMyEquipmentActivity extends AppCompatActivity {

    private Button bt_patient_equipment_add,bt_patient_equipment_return;
    private MyApplication myApplication;
    private List<AddEquipment>addEquipmentsList=new ArrayList<>();
    private ListView listView = null;
    private  AddEquipmentAdapter addEquipmentAdapter;
    private String m_name,m_state;
    /**
     * 通过适配器完成动态加载
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_my_equipment);

        initUIComponents();
        myApplication=(MyApplication)getApplication();

        addEquipmentAdapter = new AddEquipmentAdapter(PatientMyEquipmentActivity.this,R.layout.add_equipment,addEquipmentsList);
        listView = findViewById(R.id.patient_lv_list);
        listView.setAdapter(addEquipmentAdapter);
        addEquipmentAdapter.notifyDataSetChanged();
        myAddEquipment();
    }
    /**
     * 初始化控件
     */
    void   initUIComponents()
    {
        bt_patient_equipment_return=findViewById(R.id.bt_patient_equipment_return);
        bt_patient_equipment_add=findViewById(R.id.bt_patient_equipment_add);
        bt_patient_equipment_add.setOnClickListener(new addListeners());
        bt_patient_equipment_return.setOnClickListener(new addListeners());
    }
    /**
     * 连接服务器，动态加载我的设备
     */
    public void myAddEquipment(){
        Thread thread=new Thread(()->{
            Socket socket;
            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("request_type","9");
                jsonObject.put("device_type","get");
                jsonObject.put("pID",myApplication.getAccount());
                String send=jsonObject.toString();
                socket=SocketUtil.getSendSocket();
                DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(send);
                dataOutputStream.close();

                socket=SocketUtil.setPort(2030);
                DataInputStream dataInputStream=new DataInputStream(socket.getInputStream());
                String get=dataInputStream.readUTF();
                if (!get.equals("empty")){
                    JSONArray jsonArray=new JSONArray(get);
                    for (int i=jsonArray.length()-1;i>=0;i--) {
                        AddEquipment novels = findAddEquipment.sectionData(jsonArray.getJSONObject(i));
                            addEquipmentsList.add(novels);
                    }
                    //数据加载完毕，通知列表更新
                    addEquipmentAdapter.notifyDataSetChanged();
                    socket.close();
                }
            }catch (IOException | JSONException e){
                e.printStackTrace();
            }
        });
        thread.start();
        while (thread.isAlive()){
        }

    }
    /**
     * 监听事件
     * 返回，返回个人中心
     * 添加，跳转添加设备界面
     */
    private class addListeners implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_patient_equipment_return:
                    finish();
                    break;
                case R.id.bt_patient_equipment_add:
                    startActivity(new Intent(PatientMyEquipmentActivity.this,PatientAddEquipmentActivity.class));
                    break;
                default:
                    break;
            }
        }
    }
}
