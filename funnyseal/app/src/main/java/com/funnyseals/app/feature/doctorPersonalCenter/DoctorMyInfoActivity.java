package com.funnyseals.app.feature.doctorPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnyseals.app.R;
import com.funnyseals.app.SignupActivity;
import com.funnyseals.app.feature.bottomtab.DoctorBottomActivity;
import com.funnyseals.app.feature.bottomtab.PatientBottomActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DoctorMyInfoActivity extends AppCompatActivity {


    private TextView tv_doctor_info_myname,tv_doctor_info_myage,tv_doctor_info_mycompany,tv_doctor_info_mylocation,tv_doctor_info_mysex,tv_doctor_info_myposition;
    private ImageButton ib_doctor_info_return,ib_doctor_info_portrait,ib_doctor_info_password;
    private Button bt_doctor_info_changeinfo;
    private Intent intent_return,intent_portrait,intent_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_my_info);
        initUIComponents();
    }

    //更新或者说初始化，其余的写死
    void   initUIComponents()
    {
        tv_doctor_info_mysex=findViewById(R.id. tv_doctor_info_mysex);
        tv_doctor_info_myname=findViewById(R.id.tv_doctor_info_myname);
        tv_doctor_info_myage=findViewById(R.id.tv_doctor_info_myage);
        tv_doctor_info_mycompany=findViewById(R.id.tv_doctor_info_mycompany);
        tv_doctor_info_mylocation=findViewById(R.id.tv_doctor_info_mylocation);
        tv_doctor_info_myposition=findViewById(R.id.tv_doctor_info_myposition);
        ib_doctor_info_return=findViewById(R.id.ib_doctor_info_return);
        ib_doctor_info_return.setOnClickListener(new addListeners());
        ib_doctor_info_portrait=findViewById(R.id.ib_doctor_info_portrait);
        ib_doctor_info_portrait.setOnClickListener(new addListeners());
        ib_doctor_info_password=findViewById(R.id.ib_doctor_info_changepass);
        ib_doctor_info_password.setOnClickListener(new addListeners());

        tv_doctor_info_mysex.setText();
        tv_doctor_info_myname.setText();
        tv_doctor_info_myage.setText();
        tv_doctor_info_mycompany.setText();
        tv_doctor_info_mylocation.setText();
        tv_doctor_info_myposition.setText();


    }
    //监听
    private class addListeners implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ib_doctor_info_portrait:
                    intent_portrait = new Intent(DoctorMyInfoActivity.this,DoctorChangePortraitAcitivity.class);
                    startActivity( intent_portrait);
                    break;
                case  R.id.ib_doctor_info_return:
                    intent_return = new Intent(DoctorMyInfoActivity.this,DoctorPersonalCenterFragment.class);
                    startActivity( intent_return );
                    break;
                case R.id.ib_doctor_info_changepass:
                    intent_password= new Intent(DoctorMyInfoActivity.this,DoctorPasswordActivity.class);
                    startActivity(intent_password);
                    break;

                default:
                    break;
            }
        }
    }

}
