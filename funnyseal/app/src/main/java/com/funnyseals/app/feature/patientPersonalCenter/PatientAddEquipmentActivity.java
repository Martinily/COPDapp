package com.funnyseals.app.feature.patientPersonalCenter;

import android.content.Intent;
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
import com.funnyseals.app.feature.doctorPersonalCenter.DoctorMyInfoActivity;
import com.funnyseals.app.feature.doctorPersonalCenter.DoctorPassSucceedActivity;
import com.funnyseals.app.feature.doctorPersonalCenter.DoctorPasswordActivity;

public class PatientAddEquipmentActivity extends AppCompatActivity {

    private EditText ev_patient_add_name,ev_patient_add_state;
    private Button bt_patient_add_complete;
    private ImageButton ib_patient_add_return;
    private Intent intent1,intent2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_add_equipment);


    }
    private void init(){
        //获取各个edittext值
        ev_patient_add_name=findViewById(R.id.ev_patient_add_name);
        ev_patient_add_state=findViewById(R.id.ev_patient_add_state);

        ib_patient_add_return=(ImageButton)findViewById(R.id.ib_patient_add_return);
        ib_patient_add_return.setOnClickListener(new addListeners());
        bt_patient_add_complete=(Button)findViewById(R.id. bt_patient_add_complete);
        bt_patient_add_complete.setOnClickListener(new addListeners());
    }
    //监听
    private class addListeners implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ib_patient_add_return:
                    intent1 = new Intent(PatientAddEquipmentActivity.this,PatientMyEquipmentActivity.class);
                    startActivity( intent1);
                    break;
                case  R.id. bt_patient_add_complete:
                    intent2= new Intent(PatientAddEquipmentActivity.this,PatientMyEquipmentActivity.class);
                    startActivity( intent2);
                    break;
                default:
                    break;
            }
        }
    }
}
