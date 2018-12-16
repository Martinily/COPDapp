package com.funnyseals.app.feature.health;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.load.engine.Resource;
import com.funnyseals.app.R;
import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.feature.bottomtab.PatientBottomActivity;
import com.funnyseals.app.util.SocketUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HealthCenterUpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_center_update);
        //获取控件
        TextView textView1=findViewById(R.id.updatedata1);
        TextView textView2=findViewById(R.id.updatedata2);
        TextView textView3=findViewById(R.id.updatedata3);
        Button button1=findViewById(R.id.submit);
        Button button2=findViewById(R.id.cancel);
        MyApplication application=(MyApplication)this.getApplication();
        //取更新的值
        String m_data1=textView1.getText().toString();
        if (m_data1.isEmpty()) m_data1="null";
        final String m_dataf1=m_data1;
        String m_data2=textView2.getText().toString();
        if (m_data2.isEmpty()) m_data2="null";
        final String m_dataf2=m_data2;
        String m_data3=textView3.getText().toString();
        if (m_data3.isEmpty()) m_data3="null";
        final String m_dataf3=m_data3;
        //设置按钮事件
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String m_id=application.getAccount();
                new Thread(()->{
                    Socket socket;
                    try {
                        JSONObject jsonObject=new JSONObject();
                        jsonObject.put("request_type","11");
                        jsonObject.put("data_type","add");
                        jsonObject.put("pID",m_id);
                        jsonObject.put("FEV1",m_dataf1);
                        jsonObject.put("FVC",m_dataf2);
                        jsonObject.put("VC",m_dataf3);
                        String m_send=jsonObject.toString();
                        socket=SocketUtil.getSendSocket();
                        DataOutputStream out=new DataOutputStream(socket.getOutputStream());
                        out.writeUTF(m_send);
                        out.close();
                    }catch (IOException | JSONException e){
                        e.printStackTrace();
                    }
                }).start();
                finish();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
