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

public class PatientChangePortraitActivity extends AppCompatActivity {

    private ImageButton ib_patient_portrait_return;
    private Button bt_patient_portrait_complete,bt_patient_portarit_choice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_change_portrait);
        addListeners();

    }
    private  void  addListeners(){

    }

}
