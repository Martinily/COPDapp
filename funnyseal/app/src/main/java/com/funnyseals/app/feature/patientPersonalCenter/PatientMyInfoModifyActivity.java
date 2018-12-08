package com.funnyseals.app.feature.patientPersonalCenter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.funnyseals.app.R;

public class PatientMyInfoModifyActivity extends AppCompatActivity {

    private EditText  ed_patient_modify_myname,ed_patient_modify_mysex,ed_patient_modify_myage,ed_patient_modify_location;
    private Button bt_patient_modify_complete;
    private ImageButton ib_patient_modify_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_patient_my_info_modify);

        ed_patient_modify_myname=(EditText)findViewById(R.id.ed_patient_modify_myname);
        ed_patient_modify_mysex=(EditText)findViewById(R.id.ed_patient_modify_mysex);
        ed_patient_modify_myage=(EditText)findViewById(R.id.ed_patient_modify_myage);
        ed_patient_modify_location=(EditText)findViewById(R.id.ed_patient_modify_location);

        bt_patient_modify_complete=(Button)findViewById(R.id.bt_patient_modify_complete);
        ib_patient_modify_return=(ImageButton)findViewById(R.id.ib_patient_modify_return);

        addListeners();
    }

    //监听
    private  void addListeners(){



    }

}
