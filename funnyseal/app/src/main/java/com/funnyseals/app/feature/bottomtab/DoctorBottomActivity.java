package com.funnyseals.app.feature.bottomtab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.feature.doctorMessage.DoctorMessageListFragment;
import com.funnyseals.app.feature.doctorNursingPlan.DoctorNursingPlanFragment;
import com.funnyseals.app.feature.doctorPersonalCenter.DoctorPersonalCenterFragment;
import com.funnyseals.app.feature.list.PatientListFragment;
import com.funnyseals.app.model.User;
import com.funnyseals.app.util.SocketUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class DoctorBottomActivity extends AppCompatActivity {

    private RadioButton mIndexTab;

    private int        mPreviousTabId;
    private String     mMyPatient;
    private List<User> mAllMyPatient;

    private DoctorBottomTabAdapter mFragmentTabAdapter;
    private MyApplication          myApplication;

    private List<String> mMedicineNames        = new ArrayList<>();
    private List<String> mMedicineAttentions   = new ArrayList<>();
    private List<String> mInstrumentNames      = new ArrayList<>();
    private List<String> mInstrumentAttentions = new ArrayList<>();
    private Thread       thread                = new Thread(() -> {
        String send;
        Socket socket;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("request_type", "12");
            jsonObject.put("docID", myApplication.getAccount());
            send = jsonObject.toString();
            socket = SocketUtil.getSendSocket();
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(send);
            out.close();

            Thread.sleep(1000);
            socket = SocketUtil.getGetArraySocket();
            DataInputStream in = new DataInputStream(socket.getInputStream());
            String message = in.readUTF();
            if (message.contains("empty") && message.length() < 10) {
                mAllMyPatient = null;
            } else {
                mAllMyPatient = new ArrayList<>();
                JSONArray allMyPatient = new JSONArray(message);
                int i;
                for (i = 0; i < allMyPatient.length(); i++) {
                    JSONObject patient = allMyPatient.getJSONObject(i);
                    mAllMyPatient.add(new User(patient.getString("pID"),
                            patient.getString("pName"),
                            patient.getString("pSex"),
                            Integer.valueOf(patient.getString("pAge")),
                            patient.getString("pTime"),
                            patient.getString("pAddress")));
                }
            }
            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();
        } catch (JSONException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        Thread.interrupted();
    });
    private Thread       thread2               = new Thread(() -> {
        Socket socket;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("request_type", "13");
            jsonObject.put("base_type", "med");
            socket = SocketUtil.getSendSocket();
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(jsonObject.toString());
            out.close();

            Thread.sleep(4000);

            socket = SocketUtil.getArraySendSocket();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String message = dataInputStream.readUTF();
            System.err.println(message);
            if (message.equals("empty")) {
                return;
            }

            JSONArray jsonArray = new JSONArray(message);
            int i;

            for (i = 0; i < jsonArray.length(); i++) {
                mMedicineNames.add(jsonArray.getJSONObject(i).getString("medicineName"));
                mMedicineAttentions.add(jsonArray.getJSONObject(i).getString
                        ("medicineRemarks"));
                //添加一个获取注意事项
            }
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
        } catch (JSONException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        Thread.interrupted();
    });
    private Thread       thread3               = new Thread(() -> {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Socket socket;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("request_type", "13");
            jsonObject.put("base_type", "app");
            socket = SocketUtil.getSendSocket();
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(jsonObject.toString());
            out.close();

            Thread.sleep(4000);

            socket = SocketUtil.getArraySendSocket2();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String message = dataInputStream.readUTF();
            System.err.println(message);
            if (message.equals("empty")) {
                return;
            }

            JSONArray jsonArray = new JSONArray(message);
            int i;

            for (i = 0; i < jsonArray.length(); i++) {
                mInstrumentNames.add(jsonArray.getJSONObject(i).getString("apparatusName"));
                mInstrumentAttentions.add(jsonArray.getJSONObject(i).getString
                        ("apparatusRemarks"));
            }
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
        } catch (JSONException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        Thread.interrupted();
    });


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_bottom);

        myApplication = (MyApplication) getApplication();
        myApplication.setBottom(this);
        mMyPatient = "0";

        initData();
        initBottomTabs();
    }

    @Override
    public void onBackPressed () {
        if (mIndexTab.isChecked()) {
            super.onBackPressed();
        } else {
            mIndexTab.setChecked(true);
        }
    }

    public void initData () {
        thread.start();
        thread2.start();
        thread3.start();

        while (thread3.isAlive() || thread2.isAlive() || thread.isAlive()) {

        }
    }

    public void getPatient () {
        Thread thread = new Thread(() -> {
            String send;
            Socket socket;
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("request_type", "12");
                jsonObject.put("docID", myApplication.getAccount());
                send = jsonObject.toString();
                socket = SocketUtil.getSendSocket();
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(send);
                out.close();

                Thread.sleep(1000);
                socket = SocketUtil.getGetArraySocket();
                DataInputStream in = new DataInputStream(socket.getInputStream());
                String message = in.readUTF();
                if (message.contains("empty") && message.length() < 10) {
                    mAllMyPatient = null;
                } else {
                    mAllMyPatient = new ArrayList<>();
                    JSONArray allMyPatient = new JSONArray(message);
                    int i;
                    for (i = 0; i < allMyPatient.length(); i++) {
                        JSONObject patient = allMyPatient.getJSONObject(i);
                        mAllMyPatient.add(new User(patient.getString("pID"),
                                patient.getString("pName"),
                                patient.getString("pSex"),
                                Integer.valueOf(patient.getString("pAge")),
                                patient.getString("pTime"),
                                patient.getString("pAddress")));
                    }
                }
                socket.shutdownOutput();
                socket.shutdownInput();
                socket.close();
            } catch (JSONException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        while (thread.isAlive()){

        }
    }

    public List<String> getmMedicineNames () { return mMedicineNames; }

    public List<String> getmMedicineAttentions () { return mMedicineAttentions; }

    public List<String> getmInstrumentNames () { return mInstrumentNames; }

    public List<String> getmInstrumentAttentions () { return mInstrumentAttentions; }

    public List<User> getAllMyPatient () {
        return mAllMyPatient;
    }

    @Override
    protected void onResume () {
        super.onResume();

        showPrevious();
    }

    private void initBottomTabs () {
        mIndexTab = findViewById(R.id.doctor_personalCenter_tab);
        mPreviousTabId = R.id.doctor_personalCenter_tab;

        RadioGroup bottomTabs = findViewById(R.id.doctor_bottom_tabs);

        SparseArray<Fragment> fragmentMap = new SparseArray<>();

        fragmentMap.put(R.id.doctor_patientList_tab, new PatientListFragment());
        fragmentMap.put(R.id.doctor_message_tab, new DoctorMessageListFragment());
        fragmentMap.put(R.id.doctor_nursingPlan_tab, new DoctorNursingPlanFragment());
        fragmentMap.put(R.id.doctor_personalCenter_tab, new DoctorPersonalCenterFragment());

        mFragmentTabAdapter = new BottomTabBar(this, fragmentMap, R.id.fl_doctor_content,
                bottomTabs);
    }

    public void showFragmentTab (int tabID) {
        mFragmentTabAdapter.setFragmentTab(tabID);
    }

    public String getMyFriend () {
        return mMyPatient;
    }

    public void setMyFriend (String myPatient) {
        this.mMyPatient = myPatient;
    }

    public void showPrevious () {
        showFragmentTab(mPreviousTabId);
    }

    public void toNursingPlan () {
        ((RadioButton) findViewById(R.id.doctor_nursingPlan_tab)).setChecked(true);
    }

    private class BottomTabBar extends DoctorBottomTabAdapter {

        BottomTabBar (FragmentActivity fragmentActivity, SparseArray<Fragment> fragmentMap, int
                fragmentContentId, RadioGroup radioGroup) {
            super(fragmentActivity, fragmentMap, fragmentContentId, radioGroup);
        }

        @Override
        public boolean onTabWillChange (int tabId) {

            mPreviousTabId = tabId;
            return true;
        }

        @Override
        public void onTabChanged (int tabId) {

        }
    }
}
