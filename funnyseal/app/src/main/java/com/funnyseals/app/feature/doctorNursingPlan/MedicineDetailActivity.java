package com.funnyseals.app.feature.doctorNursingPlan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.funnyseals.app.R;

public class MedicineDetailActivity extends AppCompatActivity {

    private TextView mTv;
    private Button   mQuit_button;
    private Button   mDone_button;
    private CheckBox mBmorning = null;
    private CheckBox mAmorning = null;
    private CheckBox mBlunch   = null;
    private CheckBox mAlunch   = null;
    private CheckBox mBdinner  = null;
    private CheckBox mAdinner  = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_detail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mBmorning = (CheckBox) findViewById(R.id.aa);
        mAmorning = (CheckBox) findViewById(R.id.bb);
        mBlunch = (CheckBox) findViewById(R.id.cc);
        mAlunch = (CheckBox) findViewById(R.id.dd);
        mBdinner = (CheckBox) findViewById(R.id.ee);
        mAdinner = (CheckBox) findViewById(R.id.ff);
        final int[] time = {0, 0, 0, 0, 0, 0};
        mBmorning.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                Toast.makeText(MedicineDetailActivity.this, "bmorning", Toast.LENGTH_SHORT).show();
                time[0] = 1;
            } else
                time[0] = 0;
        });

        mAmorning.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                Toast.makeText(MedicineDetailActivity.this, "amorning", Toast.LENGTH_SHORT).show();
                time[1] = 1;
            } else
                time[1] = 0;
        });

        mBlunch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                Toast.makeText(MedicineDetailActivity.this, "blunch", Toast.LENGTH_SHORT).show();
                time[2] = 1;
            } else
                time[2] = 0;
        });

        mAlunch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                Toast.makeText(MedicineDetailActivity.this, "alunch", Toast.LENGTH_SHORT).show();
                time[3] = 1;
            } else
                time[3] = 0;
        });

        mBdinner.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                Toast.makeText(MedicineDetailActivity.this, "bdinner", Toast.LENGTH_SHORT).show();
                time[4] = 1;
            } else
                time[4] = 0;
        });

        mAdinner.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                Toast.makeText(MedicineDetailActivity.this, "adinner", Toast.LENGTH_SHORT).show();
                time[5] = 1;
            } else
                time[5] = 0;
        });

        final EditText medicinenum = (EditText) findViewById(R.id.medicinenum);
        final EditText medicine_editor_detail = (EditText) findViewById(R.id.medicine_editor_detail);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mTv = findViewById(R.id.medicinename);
        mTv.setText(bundle.getString("medicinename"));
        mQuit_button = findViewById(R.id.quititmedicine);
        mDone_button = findViewById(R.id.donemedicine);
        mQuit_button.setOnClickListener(v -> {
            finish();
        });
        mDone_button.setOnClickListener(v -> {

            if ((time[0] * 100000 + time[1] * 10000 + time[2] * 1000 + time[3] * 100 + time[4] * 10 + time[5]) == 0) {
                Toast.makeText(MedicineDetailActivity.this, "请选择时间", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(medicinenum.getText())) {
                Toast.makeText(MedicineDetailActivity.this, "请填写剂量", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(medicine_editor_detail.getText())) {
                Toast.makeText(MedicineDetailActivity.this, "请填写注意事项", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MedicineDetailActivity.this, String.valueOf(time[0] * 100000 + time[1] * 10000 + time[2] * 1000 + time[3] * 100 + time[4] * 10 + time[5]), Toast.LENGTH_SHORT).show();
            }

            //finish();
        });
    }
}
