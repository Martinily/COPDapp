package com.funnyseals.app.feature.doctorNursingPlan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.funnyseals.app.R;

/*
 *insturment详细内容
 */
public class InstrumentDetailActivity extends AppCompatActivity {

    private TextView mTv;
    private Button   mQuit_button;
    private Button   mDone_button;

    private TextView mDialoginstrumentname;
    private EditText mDialoginstrumentnum, mDialoginstrument_editor_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrument_detail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Intent intent =getIntent();
        Bundle bundle=intent.getExtras();

        final String position=bundle.getString("position");
        mDialoginstrumentname= (TextView)findViewById(R.id.instrumentname);
        mDialoginstrumentname.setText(bundle.getString("instrumentname"));
        mDialoginstrumentnum=(EditText)findViewById(R.id.instrumentnum);
        mDialoginstrumentnum.setText(bundle.getString("instrumentcontent"));
        mDialoginstrument_editor_detail=(EditText)findViewById(R.id.instrument_editor_detail);
        mDialoginstrument_editor_detail.setText(bundle.getString("instrumentattention"));

        mQuit_button = findViewById(R.id.quititinstrument);
        mDone_button = findViewById(R.id.doneinstrument);
        //返回按钮的监听
        mQuit_button.setOnClickListener(v -> {
            finish();
        });
        //完成按钮的监听
        mDone_button.setOnClickListener(v -> {
            Intent intent2 = new Intent();
            Bundle bundle2=new Bundle();
            bundle2.putCharSequence("reposition",position);
            bundle2.putCharSequence("reinstrumentnum",mDialoginstrumentnum.getText().toString());
            bundle2.putCharSequence("reinstrumentattention",mDialoginstrument_editor_detail.getText().toString());
            intent2.putExtras(bundle2);
            setResult(1001, intent2);
            finish();  //关闭此activity
        });
    }
}
