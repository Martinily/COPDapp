package com.funnyseals.app.feature.patientPersonalCenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
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

    private Button bt_patient_equipment_add, bt_patient_equipment_return;
    private MyApplication       myApplication;
    private List<AddEquipment>  addEquipmentsList = new ArrayList<>();
    private ListView            listView          = null;
    private AddEquipmentAdapter addEquipmentAdapter;
    private String              eName;
    private AddEquipment novels;
    private int myPosition;


    /**
     * 通过适配器完成动态加载
     */
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_my_equipment);
        initUIComponents();
        myApplication = (MyApplication) getApplication();
        addEquipmentAdapter = new AddEquipmentAdapter(PatientMyEquipmentActivity.this, R.layout
                .add_equipment, addEquipmentsList);
        listView = findViewById(R.id.patient_lv_list);
        listView.setAdapter(addEquipmentAdapter);
        addEquipmentAdapter.notifyDataSetChanged();
        myAddEquipment();
        this.registerForContextMenu(listView);
    }

    /**
     * 初始化控件
     */
    void initUIComponents () {
        bt_patient_equipment_return = findViewById(R.id.bt_patient_equipment_return);
        bt_patient_equipment_add = findViewById(R.id.bt_patient_equipment_add);

        bt_patient_equipment_add.setOnClickListener(new addListeners());
        bt_patient_equipment_return.setOnClickListener(new addListeners());

    }
    /**
     * 添加设备界面返回的时候，更新界面
     */
    @Override
    public void onResume () {
        super.onResume();
        addEquipmentAdapter.notifyDataSetChanged();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("请选择您的操作");
        menu.setHeaderIcon(android.R.drawable.ic_menu_info_details);
            /*
           参数1:组id
           参数2:项id
           参数3:顺序
           参数4:菜单项的内容
        */
        menu.add(0, 0, Menu.NONE, "删除该项");
        menu.add(0, 1, Menu.NONE, "取消");
    }


    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        myPosition=menuInfo.position;

        switch (item.getItemId()){
            case 0:
                addEquipmentAdapter.remove(addEquipmentAdapter.getItem(myPosition));
                View view = listView.getChildAt(myPosition);
                TextView t = view.findViewById(R.id.add_equipment_name);
                eName = t.getText().toString();
                deleteEquipment();
                Log.d("1", "onContextItemSelected: 删除该项sobj:111111="+addEquipmentAdapter.getItem(myPosition)+"11111");
                Log.d("2","ddddddd"+eName+"ddddd");

                return true;
            case 1:
                return false;
        }
        return super.onContextItemSelected(item);
    }
    /**
     * 连接服务器，动态加载我的设备
     */
    public void myAddEquipment () {
        Thread thread = new Thread(() -> {
            Socket socket;
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("request_type", "9");
                jsonObject.put("device_type", "get");
                jsonObject.put("pID", myApplication.getAccount());
                String send = jsonObject.toString();
                socket = SocketUtil.getSendSocket();
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(send);
                dataOutputStream.close();

                Thread.sleep(1000);
                socket = SocketUtil.setPort(2030);
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String get = dataInputStream.readUTF();
                if (!get.equals("empty")) {
                    JSONArray jsonArray = new JSONArray(get);
                    for (int i = jsonArray.length() - 1; i >= 0; i--) {
                        novels = findAddEquipment.sectionData(jsonArray
                                .getJSONObject(i));
                        addEquipmentsList.add(novels);
                    }
                    //数据加载完毕，通知列表更新
                    addEquipmentAdapter.notifyDataSetChanged();
                    socket.close();
                }
            } catch (IOException | JSONException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        while (thread.isAlive()) {
        }

    }
    /**
     * 删除
     */
    public void deleteEquipment(){
        @SuppressLint("ShowToast") Thread thread = new Thread(() -> {
            Socket socket;
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("request_type", "9");
                jsonObject.put("device_type", "delete");
                jsonObject.put("pID", myApplication.getAccount());
                jsonObject.put("eName",eName);
                String send = jsonObject.toString();
                socket = SocketUtil.getSendSocket();
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(send);
                dataOutputStream.close();

                Thread.sleep(1000);
                socket = SocketUtil.setPort(2030);
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String delete = dataInputStream.readUTF();
                switch (delete) {
                    case "false":
                        Looper.prepare();
                        Toast.makeText(PatientMyEquipmentActivity.this,"删除失败，网络连接失败",Toast.LENGTH_SHORT);
                        Looper.loop();
                        break;
                    case "true":
                        Looper.prepare();
                        Toast.makeText(PatientMyEquipmentActivity.this,"删除成功",Toast.LENGTH_SHORT);
                        Looper.loop();
                        addEquipmentAdapter.notifyDataSetChanged();
                        break;
                }
                socket.close();
            } catch (IOException | JSONException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        while (thread.isAlive()) {
        }}
    /**
     * 监听事件
     * 返回，返回个人中心
     * 添加，跳转添加设备界面
     */
    private class addListeners implements View.OnClickListener {

        @Override
        public void onClick (View v) {
            switch (v.getId()) {
                case R.id.bt_patient_equipment_return:
                    finish();
                    break;
                case R.id.bt_patient_equipment_add:
                    startActivity(new Intent(PatientMyEquipmentActivity.this, PatientAddEquipmentActivity.class));
                    break;
                default:
                    break;
            }
        }
    }
}
