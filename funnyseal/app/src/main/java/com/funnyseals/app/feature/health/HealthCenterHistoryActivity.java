package com.funnyseals.app.feature.health;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.bottomtab.PatientBottomActivity;

public class HealthCenterHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_center_history);
        Button button=findViewById(R.id.gotocentre1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HealthCenterHistoryActivity.this,PatientBottomActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}


