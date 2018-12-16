package com.funnyseals.app.feature.patientPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import com.funnyseals.app.R;

import java.util.List;

public class PatientMyEquipmentActivity extends AppCompatActivity {

    private Button bt_patient_equipment_add,bt_patient_equipment_return;
    private Intent intent1,intent2;
    private ViewPager vp_patient_equipment_addPage;
    private List<Fragment>  mlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_my_equipment);
        initUIComponents();
    }

    void   initUIComponents()
    {
        bt_patient_equipment_return=findViewById(R.id.bt_patient_equipment_return);
        bt_patient_equipment_add=findViewById(R.id.bt_patient_equipment_add);
        bt_patient_equipment_add.setOnClickListener(new addListeners());
        bt_patient_equipment_return.setOnClickListener(new addListeners());
        vp_patient_equipment_addPage=findViewById(R.id.vp_patient_equipment_addPage);

    }
    //监听
    private class addListeners implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_patient_equipment_return:
                    finish();
                    break;
                case R.id.bt_patient_equipment_add:
                    intent2 = new Intent(PatientMyEquipmentActivity.this,PatientAddEquipmentActivity.class);
                    startActivity(intent2);
                    break;
                default:
                    break;
            }
        }
    }
}
