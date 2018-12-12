package com.funnyseals.app.feature.health;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.bottomtab.PatientBottomActivity;

public class HealthCenterHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_center_history);
        String[] m_time={"134","3247","42"};
        //找到组件
        Spinner spinner=findViewById(R.id.history);
        TextView m_data1=findViewById(R.id.data1);
        TextView m_data2=findViewById(R.id.data2);
        TextView m_datacaculate=findViewById(R.id.data_caculate);
        TextView m_data3=findViewById(R.id.data3);
        Button button=findViewById(R.id.gotocentre1);
        //设置选择框
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,m_time);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String m_choice=(String) spinner.getSelectedItem();
                m_data1.setText(m_choice);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //设置返回按钮
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}


