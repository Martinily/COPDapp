package com.funnyseals.app.feature.patientPersonalCenter;

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

    private TextView tv_patient_doctor_name,tv_patient_doctor_number,tv_patient_doctor_percentage,tv_patient_doctor_fraction;
    private ImageButton ib_patient_doctor_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_my_doctor);
        ib_patient_doctor_return=(ImageButton)findViewById(R.id.imageButton);
        initUIComponents();
        addListeners();
    }
    //更新，其余写死
    void   initUIComponents()
    {
        tv_patient_doctor_name=findViewById(R.id.tv_doctor_perinfo);
        tv_patient_doctor_number=findViewById(R.id.tv_doctor_perinfo);
        tv_patient_doctor_percentage=findViewById(R.id.tv_doctor_perinfo);
        tv_patient_doctor_fraction=findViewById(R.id.tv_doctor_perinfo);

        tv_patient_doctor_name.setText();
        tv_patient_doctor_number.setText();
        tv_patient_doctor_percentage.setText();
        tv_patient_doctor_fraction.setText();


    }
    //监听
    private void addListeners() {

    }

}
