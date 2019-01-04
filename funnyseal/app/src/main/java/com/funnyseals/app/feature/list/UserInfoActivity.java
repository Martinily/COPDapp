package com.funnyseals.app.feature.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.doctorMessage.DoctorChatActivity;
import com.funnyseals.app.model.User;
import com.funnyseals.app.util.SocketUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/*
展示患者详细信息
 */
public class UserInfoActivity extends AppCompatActivity {
    private Button   mBtnReturn;
    private Button   mBtnSendMessage;
    private TextView mTvName;
    private TextView mTvSex;
    private TextView mTvAge;
    private TextView mTvAccount;
    private TextView mTvAddress;
    private TextView mTvHistory;
    private TextView mTvOrder;

    private User mMyPatient;

    private String mHistory;
    private String mOrder;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Intent intent = getIntent();
        mMyPatient = (User) intent.getSerializableExtra("user");
        initView();
        getserverdata();
        initData();
        addListener();
    }

    private void initView () {
        mBtnReturn = findViewById(R.id.gotomainlist);
        mBtnSendMessage = findViewById(R.id.sendmessage);
        mTvName = findViewById(R.id.patientname);
        mTvSex = findViewById(R.id.patientsex);
        mTvAge = findViewById(R.id.patientage);
        mTvAccount=findViewById(R.id.patientaccount);
        mTvAddress = findViewById(R.id.patientlocation);
        mTvHistory = findViewById(R.id.patientmedicalhistory);
        mTvOrder = findViewById(R.id.patientmedicalorder);
    }

    private void getserverdata(){
        Thread thread=new Thread(()->{
            Socket socket;
            String mAccount=mMyPatient.getAccount();
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("request_type","15");
                jsonObject.put("history_type","getnow");
                jsonObject.put("pID",mAccount);
                String send = jsonObject.toString();
                socket = SocketUtil.getSendSocket();
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(send);

                Thread.sleep(500);
                socket = SocketUtil.getGetSocket();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String get = dataInputStream.readUTF();
                if (!get.equals("empty")){
                    jsonObject = new JSONObject(get);
                    mHistory=jsonObject.getString("HistoryCondition");
                    mOrder=jsonObject.getString("HistoryAdvice");
                }

                socket.shutdownOutput();
                socket.shutdownInput();
                socket.close();
            }catch (JSONException | IOException | InterruptedException e){
                e.printStackTrace();
            }
        });
        thread.start();
        while (thread.isAlive()){
        }
    }

    private void initData () {
        mTvName.setText(mMyPatient.getName());
        mTvSex.setText(mMyPatient.getSex());
        mTvAge.setText(String.valueOf(mMyPatient.getAge()));
        mTvAccount.setText(mMyPatient.getAccount());
        mTvAddress.setText(mMyPatient.getAddress());
        mTvHistory.setText(mHistory);
        mTvOrder.setText(mOrder);
    }

    private void addListener () {
        mBtnReturn.setOnClickListener(v -> finish());
        mBtnSendMessage.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, DoctorChatActivity.class);
            intent.putExtra("myPatient", mMyPatient);
            startActivity(intent);
            finish();
        });
    }
}
