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

public class PatientPasswordActivity extends AppCompatActivity {

    private Button bt_patient_change_complete;
    private ImageButton ib_patient_change_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_password);

        bt_patient_change_complete=(Button)findViewById(R.id.bt_patient_change_complete);
        ib_patient_change_return=(ImageButton)findViewById(R.id.ib_patient_change_return);
        addListeners();

    }
    //监听
    private void addListeners() {

    }

}
