package com.funnyseals.app.feature.doctorNursingPlan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.funnyseals.app.R;
import com.funnyseals.app.util.BtnClickLimitUtil;

/*
医生端药物的详细制定
 */
public class MedicineDetailActivity extends AppCompatActivity {

    private Button   mQuit_button;
    private Button   mDone_button;
    private CheckBox mBmorning = null;
    private CheckBox mAmorning = null;
    private CheckBox mBlunch   = null;
    private CheckBox mAlunch   = null;
    private CheckBox mBdinner  = null;
    private CheckBox mAdinner  = null;

    private TextView mDialogmedicinename;
    private EditText mDialogmedicinenum, mDialogmedicine_editor_detail;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_detail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        final String position = bundle.getString("position");
        mDialogmedicinename = findViewById(R.id.medicinename);
        mDialogmedicinename.setText(bundle.getString("medicinename"));
        mDialogmedicinenum = findViewById(R.id.medicinenum);
        mDialogmedicinenum.setText(bundle.getString("medicinecontent"));
        mDialogmedicine_editor_detail = findViewById(R.id.medicine_editor_detail);
        mDialogmedicine_editor_detail.setText(bundle.getString("medicineattention"));
        final String medicinetime = bundle.getString("medicinetime");

        //初始化checkbox状态
        final int[] time = {0, 0, 0, 0, 0, 0};
        mBmorning = findViewById(R.id.bmorning);
        if (medicinetime != null && medicinetime.charAt(0) == '1') {
            mBmorning.setChecked(true);
            time[0] = 1;
        } else {
            mBmorning.setChecked(false);
            time[0] = 0;
        }
        mAmorning = findViewById(R.id.amorning);
        if (medicinetime != null && medicinetime.charAt(1) == '1') {
            mAmorning.setChecked(true);
            time[1] = 1;
        } else {
            mAmorning.setChecked(false);
            time[1] = 0;
        }
        mBlunch = findViewById(R.id.blunch);
        if (medicinetime != null && medicinetime.charAt(2) == '1') {
            mBlunch.setChecked(true);
            time[2] = 1;
        } else {
            mBlunch.setChecked(false);
            time[2] = 0;
        }
        mAlunch = findViewById(R.id.alunch);
        if (medicinetime != null && medicinetime.charAt(3) == '1') {
            mAlunch.setChecked(true);
            time[3] = 1;
        } else {
            mAlunch.setChecked(false);
            time[3] = 0;
        }
        mBdinner = findViewById(R.id.bdinner);
        if (medicinetime != null && medicinetime.charAt(4) == '1') {
            mBdinner.setChecked(true);
            time[4] = 1;
        } else {
            mBdinner.setChecked(false);
            time[4] = 0;
        }
        mAdinner = findViewById(R.id.adinner);
        if (medicinetime != null && medicinetime.charAt(5) == '1') {
            mAdinner.setChecked(true);
            time[5] = 1;
        } else {
            mAdinner.setChecked(false);
            time[5] = 0;
        }

        //判断时间checkbox的点击事件
        mBmorning.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                time[0] = 1;
            } else
                time[0] = 0;
        });

        mAmorning.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                time[1] = 1;
            } else
                time[1] = 0;
        });

        mBlunch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                time[2] = 1;
            } else
                time[2] = 0;
        });

        mAlunch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                time[3] = 1;
            } else
                time[3] = 0;
        });

        mBdinner.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                time[4] = 1;
            } else
                time[4] = 0;
        });

        mAdinner.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                time[5] = 1;
            } else
                time[5] = 0;
        });

        final EditText medicinenum = findViewById(R.id.medicinenum);
        final EditText medicine_editor_detail = findViewById(R.id.medicine_editor_detail);

        mQuit_button = findViewById(R.id.quititmedicine);
        mDone_button = findViewById(R.id.donemedicine);
        //退出按钮的点击
        mQuit_button.setOnClickListener(v -> {
            new AlertDialog.Builder(MedicineDetailActivity.this).setTitle("我的提示").setMessage
                    ("确定放弃此次编辑？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick (DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        });
        //药物需要在完成时进行检查并给出输入提示
        mDone_button.setOnClickListener(v -> {
            if(BtnClickLimitUtil.isFastClick()) {
                if (((time[0] * 100000 + time[1] * 10000 + time[2] * 1000 + time[3] * 100 + time[4] *

                        10 + time[5]) == 0) || TextUtils.isEmpty(medicinenum.getText()) || TextUtils
                        .isEmpty(medicine_editor_detail.getText())) {
                    new AlertDialog.Builder(MedicineDetailActivity.this).setTitle("我的提示").setMessage
                            ("仍有内容未填写，确认完成？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick (DialogInterface dialog, int which) {
                                    Intent intent2 = new Intent();
                                    Bundle bundle2 = new Bundle();
                                    bundle2.putCharSequence("reposition", position);
                                    bundle2.putCharSequence("remedicinenum", mDialogmedicinenum
                                            .getText().toString());
                                    bundle2.putCharSequence("remedicineattention",
                                            mDialogmedicine_editor_detail.getText().toString());
                                    bundle2.putCharSequence("remedicinetime", String.valueOf
                                            (time[0])
                                            + String.valueOf(time[1]) + String.valueOf(time[2]) +
                                            String.valueOf(time[3]) + String.valueOf(time[4]) +
                                            String.valueOf(time[5]));
                                    intent2.putExtras(bundle2);
                                    setResult(1001, intent2);
                                    finish();  //关闭此activity
                                }
                            }).show();
                } else {
                    Intent intent2 = new Intent();
                    Bundle bundle2 = new Bundle();
                    bundle2.putCharSequence("reposition", position);
                    bundle2.putCharSequence("remedicinenum", mDialogmedicinenum.getText().toString()
                            + "/次");
                    bundle2.putCharSequence("remedicineattention", mDialogmedicine_editor_detail
                            .getText().toString());
                    bundle2.putCharSequence("remedicinetime", String.valueOf(time[0]) + String
                            .valueOf(time[1]) + String.valueOf(time[2]) + String.valueOf(time[3]) +
                            String.valueOf(time[4]) + String.valueOf(time[5]));
                    intent2.putExtras(bundle2);
                    setResult(1001, intent2);
                    finish();  //关闭此activity
                }
            }
        });
    }
}
