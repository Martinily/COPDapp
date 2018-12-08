package com.funnyseals.app.feature.patientPersonalCenter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnyseals.app.R;

public class PatientMyInfoActivity extends AppCompatActivity {

    private TextView tv_patient_info_myname,tv_patient_info_myage,tv_patient_info_mysettlingtime,tv_patient_info_mylocation,tv_patient_info_mysex;
    private ImageButton ib_patient_info_changepass,ib_patient_info_return;
    private ImageView iv_patient_info_portrait;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_my_info);
        //加载界面

        //更新
        initUIComponents();
        //监听
        addListeners();
    }
    //更新或者说初始化，其余的写死
    void   initUIComponents()
    {
        tv_patient_info_mysex=findViewById(R.id.tv_doctor_perinfo);
        tv_patient_info_myname=findViewById(R.id.tv_doctor_perinfo);
        tv_patient_info_myage=findViewById(R.id.tv_doctor_perinfo);
        tv_patient_info_mysettlingtime=findViewById(R.id.tv_doctor_perinfo);
        tv_patient_info_mylocation=findViewById(R.id.tv_doctor_perinfo);
        tv_patient_info_mysex.setText();
        tv_patient_info_myname.setText();
        tv_patient_info_myage.setText();
        tv_patient_info_mysettlingtime.setText();
        tv_patient_info_mylocation.setText();

    }
    //监听
    private void addListeners() {


    }

}
