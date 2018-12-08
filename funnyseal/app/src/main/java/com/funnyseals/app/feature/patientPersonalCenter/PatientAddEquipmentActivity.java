package com.funnyseals.app.feature.patientPersonalCenter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.funnyseals.app.R;

public class PatientAddEquipmentActivity extends AppCompatActivity {


    private Button bt_patient_add_complete;
    private ImageButton ib_patient_add_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_add_equipment);

        addListeners();

    }
    //监听
    private void addListeners() {


    }

}
