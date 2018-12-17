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
import com.funnyseals.app.feature.health.HealthCenterFragment;
import com.funnyseals.app.feature.patientMessage.PatientMessageListFragment;
import com.funnyseals.app.feature.patientNursingPlan.PatientNursingPlanFragment;
import com.funnyseals.app.feature.patientPersonalCenter.PatientPersonalCenterFragment;
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

public class PatientBottomActivity extends AppCompatActivity {

    private RadioButton             mIndexTab;
    private int                     mPreviousTabId;
    private PatientBottomTabAdapter mFragmentTabAdapter;
    private List<User>              mAllMyDoctor;
    private MyApplication           myApplication;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_bottom);
        myApplication = (MyApplication) getApplication();
        initData();
        //初始化导航栏
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

    public void initData(){
        new Thread(() -> {
            String send;
            Socket socket;
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("request_type", "6");
                jsonObject.put("ID", myApplication.getUser().getMyDoctor());
                jsonObject.put("user_type","d");
                send = jsonObject.toString();
                socket = SocketUtil.getSendSocket();
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(send);
                out.close();

                socket=SocketUtil.getInfo();
                DataInputStream in=new DataInputStream(socket.getInputStream());
                String message=in.readUTF();
                if(message.contains("empty")&&message.length()<10){
                    mAllMyDoctor=null;
                    Thread.interrupted();
                }
                mAllMyDoctor=new ArrayList<>();
                JSONArray allMyPatient=new JSONArray(message);
                int i=0;
                for (i=0;i<allMyPatient.length();i++){
                    JSONObject patient=allMyPatient.getJSONObject(i);
                    mAllMyDoctor.add(new User(myApplication.getUser().getMyDoctor(),
                            patient.getString("docName"),
                            patient.getString("docSex"),
                            Integer.valueOf(patient.getString("docAge")),
                            patient.getString("docTime"),
                            patient.getString("docAddress"),
                            jsonObject.getString("docCompany"),
                            jsonObject.getString("docTitle")));
                }
                socket.close();

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            Thread.interrupted();
        }).start();
    }

    public List<User> getAllMyDoctor(){
        return mAllMyDoctor;
    }

    @Override
    protected void onResume () {
        super.onResume();

        showPrevious();
    }

    private void initBottomTabs () {
        mIndexTab = findViewById(R.id.patient_nursingplan_tab);
        mPreviousTabId = R.id.patient_nursingplan_tab;

        RadioGroup bottomTabs = findViewById(R.id.patient_bottom_tabs);

        SparseArray<Fragment> fragmentMap = new SparseArray<>();
        fragmentMap.put(R.id.patient_nursingplan_tab, new PatientNursingPlanFragment());
        fragmentMap.put(R.id.patient_message_tab, new PatientMessageListFragment());
        fragmentMap.put(R.id.patient_health_tab, new HealthCenterFragment());
        fragmentMap.put(R.id.patient_personalcenter_tab, new PatientPersonalCenterFragment());

        mFragmentTabAdapter = new BottomTabBar(this, fragmentMap, R.id.fl_patient_content,
                bottomTabs);
    }

    public void showFragmentTab (int tabID) {
        mFragmentTabAdapter.setFragmentTab(tabID);
    }

    public void showPrevious () {
        showFragmentTab(mPreviousTabId);
    }

    private class BottomTabBar extends PatientBottomTabAdapter {

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
