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

public class PatientMyEquipmentActivity extends AppCompatActivity {

    private TextView tv_patient_equipment_myequip1,tv_patient_equipment_myequip2;
    private ImageButton ib_patient_equipment_return,ib_patient_equipment_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_my_equipment);
        ib_patient_equipment_return=(ImageButton)findViewById(R.id.imageButton);
        ib_patient_equipment_add=findViewById(R.id.imageButton1);
        initUIComponents();
        addListeners();

    }
    void   initUIComponents()
    {
        tv_patient_equipment_myequip1=(TextView)findViewById(R.id.tv_patient_equipment_myequip1);
        tv_patient_equipment_myequip2=(TextView)findViewById(R.id.tv_patient_equipment_myequip2);


        tv_patient_equipment_myequip1.setText();
        tv_patient_equipment_myequip2.setText();

    }
    //监听
    private void addListeners() {

    }
}
