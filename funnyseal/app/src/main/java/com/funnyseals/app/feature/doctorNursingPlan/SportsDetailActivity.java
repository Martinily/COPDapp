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
the activity that set details for sports
 */
public class SportsDetailActivity extends AppCompatActivity {

    private TextView mTv;
    private Button   mQuit_button;
    private Button   mDone_button;

    private TextView dialogsportsname;
    private EditText dialogsportsnum, dialogsports_editor_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_detail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Intent intent =getIntent();
        Bundle bundle=intent.getExtras();

        final String position=bundle.getString("position");
        dialogsportsname= (TextView)findViewById(R.id.sportsname);
        dialogsportsname.setText(bundle.getString("sportsname"));
        dialogsportsnum=(EditText)findViewById(R.id.sportsnum);
        dialogsportsnum.setText(bundle.getString("sportscontent"));
        dialogsports_editor_detail=(EditText)findViewById(R.id.sports_editor_detail);
        dialogsports_editor_detail.setText(bundle.getString("sportsattention"));

        mDone_button = findViewById(R.id.donesports);
        mQuit_button.setOnClickListener(v -> {
            finish();
        });
        mDone_button.setOnClickListener(v -> {
            Intent intent2 = new Intent();
            Bundle bundle2=new Bundle();
            bundle2.putCharSequence("reposition",position);
            bundle2.putCharSequence("resportsnum",dialogsportsnum.getText().toString());
            bundle2.putCharSequence("resportsattention",dialogsports_editor_detail.getText().toString());
            intent2.putExtras(bundle2);
            setResult(1001, intent2);
            finish();  //关闭此activity
        });
    }
}