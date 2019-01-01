package com.funnyseals.app.feature.health;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
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

public class HealthCenterUpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_center_update);
        //获取控件
        EditText editText1 = findViewById(R.id.updatedata1);
        EditText editText2 = findViewById(R.id.updatedata2);
        EditText editText3 = findViewById(R.id.updatedata3);
        Button button1 = findViewById(R.id.submit);
        Button button2 = findViewById(R.id.cancel);
        MyApplication application = (MyApplication) this.getApplication();
        //设置按钮事件
        button1.setOnClickListener(v -> {
            final String m_id = application.getAccount();
            String m_data1 = editText1.getText().toString();
            if (m_data1.isEmpty())
                m_data1 = "-1";
            final String m_dataf1 = m_data1;
            String m_data2 = editText2.getText().toString();
            if (m_data2.isEmpty())
                m_data2 = "-1";
            final String m_dataf2 = m_data2;
            String m_data3 = editText3.getText().toString();
            if (m_data3.isEmpty())
                m_data3 = "-1";
            final String m_dataf3 = m_data3;
            if (m_data1.equals("-1") & m_data2.equals("-1") & m_data3.equals("-1"))
                Toast.makeText(HealthCenterUpdateActivity.this, "请输入至少一项数据", Toast
                        .LENGTH_LONG).show();
            else if (Integer.parseInt(m_data1)>Integer.parseInt(m_data2) | Integer.parseInt(m_data1)<Integer.parseInt(m_data2)/2 |
                    Integer.parseInt(m_data3)<1000 | Integer.parseInt(m_data3)>5000)
                Toast.makeText(HealthCenterUpdateActivity.this, "请输入合理的数据", Toast
                        .LENGTH_LONG).show();
            else {
                Thread thread = new Thread(() -> {
                    Socket socket;
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("request_type", "11");
                        jsonObject.put("data_type", "add");
                        jsonObject.put("pID", m_id);
                        jsonObject.put("FEV1", m_dataf1);
                        jsonObject.put("FVC", m_dataf2);
                        jsonObject.put("VC", m_dataf3);
                        String m_send = jsonObject.toString();
                        socket = SocketUtil.getSendSocket();
                        DataOutputStream dataOutputStream = new DataOutputStream(socket
                                .getOutputStream());
                        dataOutputStream.writeUTF(m_send);
                        dataOutputStream.close();

                        socket = SocketUtil.getGetSocket();
                        DataInputStream dataInputStream = new DataInputStream(socket
                                .getInputStream());
                        String m_get = dataInputStream.readUTF();
                        jsonObject = new JSONObject(m_get);
                        if (jsonObject.getString("data_result").equals("true"))
                            showToast("数据添加成功");
                        else
                            showToast("数据添加失败");
                        socket.shutdownInput();
                        socket.shutdownOutput();
                        socket.close();
                        finish();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
        });
        button2.setOnClickListener(v -> showDialog());
    }

    private void showToast (final String msg) {
        runOnUiThread(() -> Toast.makeText(HealthCenterUpdateActivity.this, msg, Toast.LENGTH_SHORT).show());
    }

    private void showDialog(){
        final AlertDialog.Builder mDialog= new AlertDialog.Builder(HealthCenterUpdateActivity.this);
        mDialog.setTitle("提示");
        mDialog.setMessage("确定取消更新数据吗？");
        mDialog.setPositiveButton("确定", (dialog, which) -> finish());
        mDialog.setNegativeButton("返回", (dialog, which) -> dialog.dismiss());
        mDialog.show();
    }

}
