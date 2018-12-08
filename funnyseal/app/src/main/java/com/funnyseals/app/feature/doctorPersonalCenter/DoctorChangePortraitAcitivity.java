package com.funnyseals.app.feature.doctorPersonalCenter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.funnyseals.app.R;

public class DoctorChangePortraitAcitivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton ib_doctor_portrait_return;
    private Button bt_doctor_portrait_complete,bt_doctor_portrait_choice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_change_portrait);
        ib_doctor_portrait_return=(ImageButton)findViewById(R.id.ib_doctor_portrait_return);
        bt_doctor_portrait_complete=(Button)findViewById(R.id.bt_doctor_portrait_complete);
        bt_doctor_portrait_choice=(Button)findViewById(R.id.bt_doctor_portrait_choice);
        ib_doctor_portrait_return=findViewById(R.id.ib_doctor_change_return);
        ib_doctor_portrait_return.setOnClickListener();
    }

        public void onClick(View view){
            Toast.makeText(getApplicationContext(),,Toast.LENGTH_LONG).show();
        }

}
