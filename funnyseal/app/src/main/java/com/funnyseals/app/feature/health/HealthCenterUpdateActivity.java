package com.funnyseals.app.feature.health;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
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
                            Looper.prepare();
                            if (jsonObject.getString("data_result").equals("true"))
                                Toast.makeText(HealthCenterUpdateActivity.this, "数据添加成功", Toast
                                        .LENGTH_LONG).show();
                            else
                                Toast.makeText(HealthCenterUpdateActivity.this, "数据添加失败", Toast
                                        .LENGTH_LONG).show();
                            Looper.loop();
                            dataInputStream.close();
                            socket.shutdownInput();
                            socket.shutdownOutput();
                            socket.close();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    });
                    thread.start();
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finish();
            }
        });
    }
}
