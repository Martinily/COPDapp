package com.funnyseals.app.feature.doctorNursingPlan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.funnyseals.app.R;

public class SportsDetailActivity extends AppCompatActivity {

    private TextView mTv;
    private Button   mQuit_button;
    private Button   mDone_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_detail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mTv = findViewById(R.id.sportsname);
        mTv.setText(bundle.getString("sportsname"));
        mQuit_button = findViewById(R.id.quititsports);
        mDone_button = findViewById(R.id.donesports);
        mQuit_button.setOnClickListener(v -> {
            finish();
        });
        mDone_button.setOnClickListener(v -> {
            finish();
        });
    }
}
