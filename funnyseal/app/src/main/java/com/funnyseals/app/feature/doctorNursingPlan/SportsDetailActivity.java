package com.funnyseals.app.feature.doctorNursingPlan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.funnyseals.app.R;
import com.funnyseals.app.util.BtnClickLimitUtil;

/*
医生端制定详细运动计划
 */
public class SportsDetailActivity extends AppCompatActivity {

    private Button mQuit_button;
    private Button mDone_button;

    private TextView dialogsportsname;
    private EditText dialogsportsnum, dialogsports_editor_detail;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_detail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        final String position = bundle.getString("position");
        dialogsportsname = findViewById(R.id.sportsname);
        dialogsportsname.setText(bundle.getString("sportsname"));
        dialogsportsnum = findViewById(R.id.sportsnum);
        dialogsportsnum.setText(bundle.getString("sportscontent"));
        dialogsports_editor_detail = findViewById(R.id.sports_editor_detail);
        dialogsports_editor_detail.setText(bundle.getString("sportsattention"));

        final EditText sportsnum = findViewById(R.id.sportsnum);
        final EditText sports_editor_detail = findViewById(R.id.sports_editor_detail);

        mQuit_button = findViewById(R.id.quititsports);
        mDone_button = findViewById(R.id.donesports);
        //退出按钮的点击
        mQuit_button.setOnClickListener(v -> {
            new AlertDialog.Builder(SportsDetailActivity.this).setTitle("我的提示").setMessage
                    ("确定放弃此次编辑？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick (DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        });
        //完成按钮的点击
        mDone_button.setOnClickListener(v -> {
            if(BtnClickLimitUtil.isFastClick()) {
                if ((TextUtils.isEmpty(sportsnum.getText()) || TextUtils.isEmpty(sports_editor_detail

                        .getText()))) {
                    new AlertDialog.Builder(SportsDetailActivity.this).setTitle("我的提示").setMessage
                            ("仍有内容未填写，确认完成？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick (DialogInterface dialog, int which) {
                                    Intent intent2 = new Intent();
                                    Bundle bundle2 = new Bundle();
                                    bundle2.putCharSequence("reposition", position);
                                    bundle2.putCharSequence("resportsnum", dialogsportsnum.getText()
                                            .toString() );
                                    bundle2.putCharSequence("resportsattention",
                                            dialogsports_editor_detail.getText().toString());
                                    intent2.putExtras(bundle2);
                                    setResult(1001, intent2);
                                    finish();  //关闭此activity
                                }
                            }).show();
                } else {
                    Intent intent2 = new Intent();
                    Bundle bundle2 = new Bundle();
                    bundle2.putCharSequence("reposition", position);
                    bundle2.putCharSequence("resportsnum", dialogsportsnum.getText().toString() +
                            "分钟/次");
                    bundle2.putCharSequence("resportsattention", dialogsports_editor_detail
                            .getText()
                            .toString());
                    intent2.putExtras(bundle2);
                    setResult(1001, intent2);
                    finish();  //关闭此activity
                }
            }
        });
    }
}