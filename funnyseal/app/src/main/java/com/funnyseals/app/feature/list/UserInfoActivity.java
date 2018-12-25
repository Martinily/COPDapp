package com.funnyseals.app.feature.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        ImageView m_imageView = findViewById(R.id.headphoto);
        TextView m_name = findViewById(R.id.patientname);
        TextView m_sex = findViewById(R.id.patientsex);
        TextView m_age = findViewById(R.id.patientage);
        TextView m_account = findViewById(R.id.patientaccount);
        TextView m_location = findViewById(R.id.patientlocation);
        TextView m_medicalhistory = findViewById(R.id.patientmedicalhistory);
        TextView m_medicalorder = findViewById(R.id.patientmedicalorder);

        Intent intent = getIntent();
        User m_user = (User) intent.getSerializableExtra("user");

        m_name.setText(m_user.getName());
        m_sex.setText(m_user.getSex());
        m_age.setText(String.valueOf(m_user.getAge()));
        m_account.setText(m_user.getAccount());
        m_location.setText(m_user.getAddress());
        m_medicalhistory.setText(m_user.getMedicalHistory());
        m_medicalorder.setText(m_user.getMedicalOrder());

        button1.setOnClickListener(v -> finish());
        button2.setOnClickListener(v -> {
            Intent intent1 = new Intent(UserInfoActivity.this, DoctorChatActivity.class);
            System.err.println(m_user.getAccount());
            intent1.putExtra("myPatient", m_user);
            startActivity(intent1);
            finish();
        });
    }
}
