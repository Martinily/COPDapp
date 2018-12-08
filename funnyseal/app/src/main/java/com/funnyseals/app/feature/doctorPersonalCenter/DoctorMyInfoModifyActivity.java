package com.funnyseals.app.feature.doctorPersonalCenter;

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

public class DoctorMyInfoModifyActivity extends AppCompatActivity {


    private EditText et_doctor_modify_mynaame,et_doctor_modify_mysex,et_doctor_modify_myage,et_doctor_modify_mylocation,et_doctor_modify_myhospital,et_doctor_modify_mypost;
    private Button bt_doctor_modify_complete;
    private ImageButton ib_doctor_modify_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_my_info_modify);

        ib_doctor_modify_return=(ImageButton)findViewById(R.id. ib_doctor_modify_return);
        bt_doctor_modify_complete=(Button)findViewById(R.id.bt_doctor_modify_complete);
    }

    //监听
    private  void addListeners(){



    }

}
