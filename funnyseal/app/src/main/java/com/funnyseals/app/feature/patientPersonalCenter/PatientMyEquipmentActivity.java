package com.funnyseals.app.feature.patientPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import com.funnyseals.app.R;
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

public class PatientMyEquipmentActivity extends AppCompatActivity {

    private Button bt_patient_equipment_add,bt_patient_equipment_return;
    private List<AddEquipment>addEquipmentsList=new ArrayList<AddEquipment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_my_equipment);
        initUIComponents();
        AddEquipmentAdapter addEquipmentAdapter = new AddEquipmentAdapter(PatientMyEquipmentActivity.this,R.layout.add_equipment,addEquipmentsList);
        ListView listView = findViewById(R.id.patient_lv_list);
        listView.setAdapter(addEquipmentAdapter);
    }

    void   initUIComponents()
    {
        bt_patient_equipment_return=findViewById(R.id.bt_patient_equipment_return);
        bt_patient_equipment_add=findViewById(R.id.bt_patient_equipment_add);
        bt_patient_equipment_add.setOnClickListener(new addListeners());
        bt_patient_equipment_return.setOnClickListener(new addListeners());
        myAddEquipment();
    }
    public void myAddEquipment(){
        new Thread(()->{
            String send="";
            Socket socket;
            try{
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("pID","xxxxxx");
                jsonObject.put("request_type","1");
                send = jsonObject.toString();
                socket = SocketUtil.getSendSocket();
                DataOutputStream out=new DataOutputStream(socket.getOutputStream());
                out.writeUTF(send);
                out.close();

                socket = SocketUtil.getGetSocket();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String message = dataInputStream.readUTF();

                JSONArray jsonArray = new JSONArray();

                jsonObject = new JSONObject(message);
                String name=jsonObject.get("equipmentname").toString();
                String state=jsonObject.get("euipmentstate").toString();
                AddEquipment m_equipment1 = new AddEquipment(name,state);
                addEquipmentsList.add(m_equipment1);
                socket.close();
            }catch (IOException | JSONException e){
                e.printStackTrace();
            }
        }).start();
    }
    //监听
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
