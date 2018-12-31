package com.funnyseals.app.feature.patientNursingPlan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.MyApplication;
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

/**
 * 护理计划to fragment
 */
public class PatientNursingPlanFragment extends Fragment implements View.OnClickListener {
    private View                      mView;
    private TextView                  mTv_patient_one;
    private TextView                  mTv_patient_two;
    private TextView                  mTv_patient_three;
    private Button                    mPatientHistory;
    private ViewPager                 mVp_patient_myViewPager;
    private List<Fragment>            mList;
    private PatientTabFragmentAdapter mAdapter;
    private MyApplication             myApplication;
    private String                    mPatientID           = "";//传入的患者编号
    private List<String>              mMedicine_Titles     = new ArrayList<>();
    private List<String>              mMedicine_Contents   = new ArrayList<>();
    private List<String>              mMedicine_attentions = new ArrayList<>();
    private List<String>              mMedicine_needtimes  = new ArrayList<>();

    private List<String> mInstrument_Titles     = new ArrayList<>();
    private List<String> mInstrument_Contents   = new ArrayList<>();
    private List<String> mInstrument_attentions = new ArrayList<>();

    private List<String> mSports_Titles     = new ArrayList<>();//名称
    private List<String> mSports_Contents   = new ArrayList<>();  //时长
    private List<String> mSports_attentions = new ArrayList<>();//注意事项

    public void onResume () {
        super.onResume();

        mMedicine_Titles.clear();
        mMedicine_Contents.clear();
        mMedicine_attentions.clear();
        mMedicine_needtimes.clear();

        mInstrument_Titles.clear();
        mInstrument_Contents.clear();
        mInstrument_attentions.clear();

        mSports_Titles.clear();
        mSports_Contents.clear();
        mSports_attentions.clear();

        GetMessage();
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_patient_nursing_plan, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        myApplication = (MyApplication) getActivity().getApplication();
        mPatientID=myApplication.getAccount();
        GetMessage();

        // 设置菜单栏的点击事件
        mTv_patient_one.setOnClickListener(this);
        mTv_patient_two.setOnClickListener(this);
        mTv_patient_three.setOnClickListener(this);
        mVp_patient_myViewPager.setOnPageChangeListener(new MyPagerChangeListener());
        mPatientHistory.setOnClickListener(this);

        //把Fragment添加到List集合里面
        mList = new ArrayList<>();
        mList.add(new PatientOneFragment());
        mList.add(new PatientTwoFragment());
        mList.add(new PatientThreeFragment());
        mAdapter = new PatientTabFragmentAdapter(getChildFragmentManager(), mList);
        mVp_patient_myViewPager.setAdapter(mAdapter);
        mVp_patient_myViewPager.setCurrentItem(0);  //初始化显示第一个页面
        mTv_patient_one.setBackgroundColor(Color.LTGRAY);//被选中就为灰色
    }

    public void GetMessage(){
        Thread thread = new Thread(() -> {
            Socket socket;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ID", mPatientID);
                jsonObject.put("request_type", "4");
                jsonObject.put("user_type", "p");
                jsonObject.put("query_state", "now");
                socket = SocketUtil.getSendSocket();
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(jsonObject.toString());
                out.close();

                Thread.sleep(1000);

                socket = SocketUtil.getGetSocket();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String message = dataInputStream.readUTF();
                socket.close();
                if (message.equals("empty")) {
                    return;
                }

                JSONArray jsonArray = new JSONArray(message);
                int i;

                for (i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.getJSONObject(i).getString("item_type").equals("med")) {
                        mMedicine_Titles.add(jsonArray.getJSONObject(i).getString("mName"));
                        mMedicine_Contents.add(jsonArray.getJSONObject(i).getString("mDose"));
                        mMedicine_attentions.add(jsonArray.getJSONObject(i).getString
                                ("mAttention"));
                        mMedicine_needtimes.add(jsonArray.getJSONObject(i).getString("mTime"));
                    } else if (jsonArray.getJSONObject(i).getString("item_type").equals("app")) {
                        mInstrument_Titles.add(jsonArray.getJSONObject(i).getString("appName"));
                        mInstrument_Contents.add(jsonArray.getJSONObject(i).getString("appTime"));
                        mInstrument_attentions.add(jsonArray.getJSONObject(i).getString
                                ("appAttention"));
                    } else if (jsonArray.getJSONObject(i).getString("item_type").equals("sports")) {
                        mSports_Titles.add(jsonArray.getJSONObject(i).getString("sType"));
                        mSports_Contents.add(jsonArray.getJSONObject(i).getString("sTime"));
                        mSports_attentions.add(jsonArray.getJSONObject(i).getString("sAttention"));
                    }
                }
                socket.close();
            } catch (JSONException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
            Thread.interrupted();
        });
        thread.start();
        while (thread.isAlive()) {

        }
    }

    @Override
    public void onClick (View v) {

        switch (v.getId()) {
            case R.id.tv_patient_one:
                mVp_patient_myViewPager.setCurrentItem(0);
                mTv_patient_one.setBackgroundColor(Color.LTGRAY);
                mTv_patient_two.setBackgroundColor(Color.WHITE);
                mTv_patient_three.setBackgroundColor(Color.WHITE);
                break;
            case R.id.tv_patient_two:
                mVp_patient_myViewPager.setCurrentItem(1);
                mTv_patient_one.setBackgroundColor(Color.WHITE);
                mTv_patient_two.setBackgroundColor(Color.LTGRAY);
                mTv_patient_three.setBackgroundColor(Color.WHITE);
                break;
            case R.id.tv_patient_three:
                mVp_patient_myViewPager.setCurrentItem(2);
                mTv_patient_one.setBackgroundColor(Color.WHITE);
                mTv_patient_two.setBackgroundColor(Color.WHITE);
                mTv_patient_three.setBackgroundColor(Color.LTGRAY);
                break;
            case R.id.patienthistory: //查看历史计划
                Intent intent = new Intent(getActivity(), PatientHistoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("PatientID", mPatientID);//患者id
                intent.putExtras(bundle);
                startActivity(intent);
                //getActivity().finish();
                break;
        }
    }

    private void initView () {
        mTv_patient_one = getActivity().findViewById(R.id.tv_patient_one);
        mTv_patient_two = getActivity().findViewById(R.id.tv_patient_two);
        mTv_patient_three = getActivity().findViewById(R.id.tv_patient_three);
        mVp_patient_myViewPager = getActivity().findViewById(R.id.vp_patient_myViewPager);
        mPatientHistory = getActivity().findViewById(R.id.patienthistory);
    }

    public List<String> getmMedicine_Titles () { return mMedicine_Titles; }

    public List<String> getmMedicine_Contents () {
        return mMedicine_Contents;
    }

    public List<String> getmMedicine_attentions () { return mMedicine_attentions; }

    public List<String> getmMedicine_needtimes () {
        return mMedicine_needtimes;
    }

    public List<String> getmInstrument_Titles () { return mInstrument_Titles; }

    public List<String> getmInstrument_Contents () { return mInstrument_Contents; }

    public List<String> getmInstrument_attentions () { return mInstrument_attentions; }

    public List<String> getmSports_Titles () { return mSports_Titles; }

    public List<String> getmSports_Contents () { return mSports_Contents; }

    public List<String> getmSports_attentions () { return mSports_attentions; }

    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged (int arg0) {
        }

        @Override
        public void onPageScrolled (int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected (int arg0) {
            switch (arg0) {
                case 0:
                    mTv_patient_one.setBackgroundColor(Color.LTGRAY);
                    mTv_patient_two.setBackgroundColor(Color.WHITE);
                    mTv_patient_three.setBackgroundColor(Color.WHITE);
                    break;
                case 1:
                    mTv_patient_one.setBackgroundColor(Color.WHITE);
                    mTv_patient_two.setBackgroundColor(Color.LTGRAY);
                    mTv_patient_three.setBackgroundColor(Color.WHITE);
                    break;
                case 2:
                    mTv_patient_one.setBackgroundColor(Color.WHITE);
                    mTv_patient_two.setBackgroundColor(Color.WHITE);
                    mTv_patient_three.setBackgroundColor(Color.LTGRAY);
                    break;
            }
        }
    }
}