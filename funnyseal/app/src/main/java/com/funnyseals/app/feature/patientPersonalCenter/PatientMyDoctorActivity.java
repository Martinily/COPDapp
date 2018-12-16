package com.funnyseals.app.feature.patientPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.funnyseals.app.R;

public class PatientMyDoctorActivity extends AppCompatActivity {

    private TextView tv_patient_doctor_name,tv_patient_doctor_hospital,tv_patient_doctor_post;
    private ImageButton ib_patient_doctor_return;
    private Intent intent1,intent2;
    private String str_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_my_doctor);
        init();

    }
    //更新，其余写死
    void   init()
    {
        tv_patient_doctor_name=findViewById(R.id.tv_doctor_perinfo);
        tv_patient_doctor_hospital=findViewById(R.id.tv_patient_doctor_hospital);
        tv_patient_doctor_post=findViewById(R.id.tv_patient_doctor_post);

        ib_patient_doctor_return=findViewById(R.id.ib_patient_doctor_return);
        ib_patient_doctor_return.setOnClickListener(new addListeners());
    }
    //监听
    private class addListeners implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ib_patient_doctor_return:
                    finish();
                    break;
                default:
                    break;
            }
        }
    }

}
