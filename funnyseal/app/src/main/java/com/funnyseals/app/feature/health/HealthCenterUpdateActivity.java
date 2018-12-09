package com.funnyseals.app.feature.health;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.bottomtab.PatientBottomActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HealthCenterUpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_center_update);
        TextView textView1=findViewById(R.id.updatedata1);
        TextView textView2=findViewById(R.id.updatedata2);
        TextView textView3=findViewById(R.id.updatedata3);
        Button button1=findViewById(R.id.submit);
        Button button2=findViewById(R.id.cancel);
        String m_data1=textView1.getText().toString();
        String m_data2=textView2.getText().toString();
        String m_data3=textView3.getText().toString();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HealthCenterUpdateActivity.this,PatientBottomActivity.class);
                Date date=new Date();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
                String m_date=simpleDateFormat.format(date);
                Bundle bundle=new Bundle();
                bundle.putString("data1",m_data1);
                bundle.putString("data2",m_data2);
                bundle.putString("data3",m_data3);
                bundle.putString("datatime",m_date);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HealthCenterUpdateActivity.this,PatientBottomActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
