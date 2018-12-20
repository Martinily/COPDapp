package com.funnyseals.app.feature.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.doctorMessage.DoctorChatActivity;
import com.funnyseals.app.model.User;

/*
展示患者详细信息
 */
public class UserInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Button button1 = findViewById(R.id.gotomainlist);
        Button button2 = findViewById(R.id.sendmessage);
        Intent intent = getIntent();
        User m_user = (User) intent.getSerializableExtra("user");
        ImageView m_imageView = findViewById(R.id.headphoto);
        TextView m_name = findViewById(R.id.patientname);
        TextView m_sex = findViewById(R.id.patientsex);
        TextView m_age = findViewById(R.id.patientage);
        TextView m_account = findViewById(R.id.patientaccount);
        TextView m_location = findViewById(R.id.patientlocation);
        m_name.setText(m_user.getName());
        m_sex.setText(m_user.getSex());
        m_age.setText(m_user.getAge());
        m_account.setText(m_user.getAccount());
        m_location.setText(m_user.getAddress());
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finish();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(UserInfoActivity.this, DoctorChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("myPatient", m_user);
                intent.putExtras(bundle);
            }
        });
    }
}
