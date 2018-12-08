package com.funnyseals.app.feature.doctorPersonalCenter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.funnyseals.app.R;

public class DoctorPasswordActivity extends AppCompatActivity {

    private Button bt_doctor_change_complete;
    private ImageButton ib_doctor_change_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_password);

        bt_doctor_change_complete=(Button)findViewById(R.id.bt_doctor_change_complete);
        ib_doctor_change_return=(ImageButton)findViewById(R.id.ib_doctor_change_return);


        addListeners();

    }

    //监听
    private void addListeners() {


    }

}
