package com.funnyseals.app.feature.doctorNursingPlan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.funnyseals.app.R;

public class MedicineDetailActivity extends AppCompatActivity {

    private TextView tv;
    private Button   quit_button;
    private Button   done_button;

    CheckBox bmorning = null;
    CheckBox amorning = null;
    CheckBox blunch   = null;
    CheckBox alunch   = null;
    CheckBox bdinner  = null;
    CheckBox adinner  = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_detail);
        ActionBar actionBar = getSupportActionBar();   //隐藏自带actionBar
        if (actionBar != null) {
            actionBar.hide();
        }

        bmorning = (CheckBox) findViewById(R.id.aa);
        amorning = (CheckBox) findViewById(R.id.bb);
        blunch = (CheckBox) findViewById(R.id.cc);
        alunch = (CheckBox) findViewById(R.id.dd);
        bdinner = (CheckBox) findViewById(R.id.ee);
        adinner = (CheckBox) findViewById(R.id.ff);
        final int[] time = {0, 0, 0, 0, 0, 0};
        bmorning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    Toast.makeText(MedicineDetailActivity.this, "bmorning", Toast.LENGTH_SHORT).show();
                    time[0] = 1;
                } else
                    time[0] = 0;
            }
        });

        amorning.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // TODO Auto-generated method stub
            if (isChecked) {
                Toast.makeText(MedicineDetailActivity.this, "amorning", Toast.LENGTH_SHORT).show();
                time[1] = 1;
            } else
                time[1] = 0;
        });

        blunch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // TODO Auto-generated method stub
            if (isChecked) {
                Toast.makeText(MedicineDetailActivity.this, "blunch", Toast.LENGTH_SHORT).show();
                time[2] = 1;
            } else
                time[2] = 0;
        });

        alunch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    Toast.makeText(MedicineDetailActivity.this, "alunch", Toast.LENGTH_SHORT).show();
                    time[3] = 1;
                } else
                    time[3] = 0;
            }
        });

        bdinner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    Toast.makeText(MedicineDetailActivity.this, "bdinner", Toast.LENGTH_SHORT).show();
                    time[4] = 1;
                } else
                    time[4] = 0;
            }
        });

        adinner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    Toast.makeText(MedicineDetailActivity.this, "adinner", Toast.LENGTH_SHORT).show();
                    time[5] = 1;
                } else
                    time[5] = 0;
            }
        });

        final EditText medicinenum = (EditText) findViewById(R.id.medicinenum);
        final EditText medicine_editor_detail = (EditText) findViewById(R.id.medicine_editor_detail);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        tv = findViewById(R.id.medicinename);
        tv.setText(bundle.getString("medicinename"));
        quit_button = findViewById(R.id.quititmedicine);
        done_button = findViewById(R.id.donemedicine);
        quit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  //关闭此activity
            }
        });
        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((time[0] * 100000 + time[1] * 10000 + time[2] * 1000 + time[3] * 100 + time[4] * 10 + time[5]) == 0) {
                    Toast.makeText(MedicineDetailActivity.this, "请选择时间", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(medicinenum.getText())) {
                    Toast.makeText(MedicineDetailActivity.this, "请填写剂量", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(medicine_editor_detail.getText())) {
                    Toast.makeText(MedicineDetailActivity.this, "请填写注意事项", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MedicineDetailActivity.this, String.valueOf(time[0] * 100000 + time[1] * 10000 + time[2] * 1000 + time[3] * 100 + time[4] * 10 + time[5]), Toast.LENGTH_SHORT).show();
                }

                //finish();  //关闭此activity
            }
        });
    }
}
