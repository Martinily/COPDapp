package com.funnyseals.app.feature.doctorNursingPlan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.feature.bottomtab.DoctorBottomActivity;
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
import java.util.Objects;

/**
 * 医生端护理计划fragment
 */
public class DoctorNursingPlanFragment extends Fragment implements View.OnClickListener {
    private TextView   mTv_doctor_one;
    private TextView   mTv_doctor_two;
    private TextView   mTv_doctor_three;
    private ViewPager  mVp_doctor_myviewpage;
    private Button     mBtnSend;
    private Button     mBtnHistory;
    private int        mPlannum  = 0;
    private String     mDoctorID = "11111";//传入的医生编号
    private List<Bean> mAllMedicineItem;
    private List<Bean> mAllInstrumentItem;
    private List<Bean> mAllSportsItem;
    private String     mPatientId;//患者id
    private String     mJudgesubmmit;
    private String     mMyFriend;  //消息到护理计划的制定
    private String     mWhere; //标识从何处跳转过来的

    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_doctor_nursing_plan, null);
        mAllMedicineItem = new ArrayList<>();
        mAllInstrumentItem = new ArrayList<>();
        mAllSportsItem = new ArrayList<>();
        return mView;
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        // 设置菜单栏的点击事件
        mTv_doctor_one.setOnClickListener(this);
        mTv_doctor_two.setOnClickListener(this);
        mTv_doctor_three.setOnClickListener(this);
        mVp_doctor_myviewpage.setOnPageChangeListener(new MyPagerChangeListener());
        mBtnSend.setOnClickListener(this);
        mBtnHistory.setOnClickListener(this);
        //把Fragment添加到List集合里面
        List<Fragment> mList = new ArrayList<>();
        mList.add(new DoctorOneFragment());
        mList.add(new DoctorTwoFragment());
        mList.add(new DoctorThreeFragment());
        DoctorTabFragmentPagerAdapter mAdapter = new DoctorTabFragmentPagerAdapter
                (getChildFragmentManager(), mList);
        mVp_doctor_myviewpage.setAdapter(mAdapter);
        mVp_doctor_myviewpage.setCurrentItem(0);  //初始化显示第一个页面
        mTv_doctor_one.setBackgroundColor(Color.LTGRAY);//被选中就为灰色
    }

    @Override
    public void onResume () {
        super.onResume();

        mMyFriend=((DoctorBottomActivity)Objects.requireNonNull(getActivity())).getMyFriend();
        ((DoctorBottomActivity)getActivity()).setMyFriend("0");
        mWhere="1";
    }

    //点击事件的集合
    @Override
    public void onClick (View v) {

        switch (v.getId()) {
            case R.id.tv_doctor_one:
                mVp_doctor_myviewpage.setCurrentItem(0);
                mTv_doctor_one.setBackgroundColor(Color.LTGRAY);
                mTv_doctor_two.setBackgroundColor(Color.WHITE);
                mTv_doctor_three.setBackgroundColor(Color.WHITE);
                break;
            case R.id.tv_doctor_two:
                mVp_doctor_myviewpage.setCurrentItem(1);
                mTv_doctor_one.setBackgroundColor(Color.WHITE);
                mTv_doctor_two.setBackgroundColor(Color.LTGRAY);
                mTv_doctor_three.setBackgroundColor(Color.WHITE);
                break;
            case R.id.tv_doctor_three:
                mVp_doctor_myviewpage.setCurrentItem(2);
                mTv_doctor_one.setBackgroundColor(Color.WHITE);
                mTv_doctor_two.setBackgroundColor(Color.WHITE);
                mTv_doctor_three.setBackgroundColor(Color.LTGRAY);
                break;
            case R.id.doctorhistory:  //查看历史计划
                Intent intent = new Intent(getActivity(), DoctorHistoryActivity.class);
                //传输医生编号
                Bundle bundle = new Bundle();
                bundle.putString("DoctorID", mDoctorID);//医生id
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.send:   //发送计划
                if (mPlannum > 0) {
                    if (mWhere.equals("1")) {
                        new AlertDialog.Builder(getActivity()).setTitle("我的提示").setMessage("确定要发送吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick (DialogInterface dialog, int which) {
                                        mPatientId = mMyFriend;
                                        mWhere="0";
                                        SendPlan();
                                    }
                                }).show();
                    } else {
                        Intent intent2 = new Intent(getActivity(), PickPatientActivity.class);
                        //传输医生编号
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("DoctorID", mDoctorID);//医生id
                        intent2.putExtras(bundle2);
                        startActivityForResult(intent2, 1000);
                    }
                } else {
                    Toast.makeText(getActivity(), "当前计划为空！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void SendPlan () {
        Thread thread = new Thread(() -> {
            Socket socket;
            JSONArray nursingplan = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            MyApplication application = (MyApplication) getActivity().getApplication();
            try {
                jsonObject.put("request_type", "3");
                //jsonObject.put("docID",application.getAccount());
                jsonObject.put("docID", mDoctorID);// mDoctorID
                jsonObject.put("pID", mPatientId);  //mPatientId
                socket = SocketUtil.getSendSocket();
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(jsonObject.toString());
                out.close();

                Thread.sleep(1000);

                for (Bean Medicine : mAllMedicineItem) {
                    JSONObject medicine = new JSONObject();
                    medicine.put("item_type", "med");
                    medicine.put("mName", Medicine.getName());
                    if (Medicine.getContent().isEmpty()) {
                        medicine.put("mDose", "-");
                    } else {
                        medicine.put("mDose", Medicine.getContent());
                    }
                    if (Medicine.getAttention().isEmpty()) {
                        medicine.put("mAttention", "-");
                    } else {
                        medicine.put("mAttention", Medicine.getAttention());
                    }
                    if (Medicine.gettime().equals("000000")) {
                        medicine.put("mTime", "-");
                        System.err.println("meiyou");
                    } else {
                        medicine.put("mTime", Medicine.gettime());
                    }
                    nursingplan.put(medicine);
                }
                for (Bean Instrument : mAllInstrumentItem) {
                    JSONObject instrument = new JSONObject();
                    instrument.put("item_type", "app");
                    instrument.put("appName", Instrument.getName());
                    if (Instrument.getAttention().isEmpty()) {
                        instrument.put("appAttention", "-");
                    } else {
                        instrument.put("appAttention", Instrument.getAttention());
                    }
                    if (Instrument.getContent().isEmpty()) {
                        instrument.put("appTime", "-");
                    } else {
                        instrument.put("appTime", Instrument.getContent());
                    }
                    nursingplan.put(instrument);
                }
                for (Bean Sports : mAllSportsItem) {
                    JSONObject sport = new JSONObject();
                    sport.put("item_type", "sports");
                    sport.put("sType", Sports.getName());
                    if (Sports.getAttention().isEmpty()) {
                        sport.put("sAttention", "-");
                    } else {
                        sport.put("sAttention", Sports.getAttention());
                    }
                    if (Sports.getContent().isEmpty()) {
                        sport.put("sTime", "-");
                    } else {
                        sport.put("sTime", Sports.getContent());
                    }
                    nursingplan.put(sport);
                }

                socket = SocketUtil.getArraySendSocket();
                System.err.println(socket.isConnected());
                out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(nursingplan.toString());
                out.close();

                Thread.sleep(1000);

                socket = SocketUtil.getGetSocket();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                String message = dataInputStream.readUTF();
                System.err.println(message);

                JSONObject jsonObject3 = new JSONObject(message);
                mJudgesubmmit = jsonObject3.getString("item_result");

                socket.close();
            } catch (JSONException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
            Thread.interrupted();
        });
        thread.start();
        while (thread.isAlive()) {

        }

        if (mJudgesubmmit.equals("true")) {
            Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "当前网络不稳定，换个姿势试试~", Toast.LENGTH_SHORT).show();
        }
    }

    //接收患者列表返回的患者Id
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == 1001) {
            mPatientId = data.getStringExtra("patientid");
            SendPlan();
            System.err.println(mPatientId);
        }
    }

    private void initView () {
        mTv_doctor_one = getActivity().findViewById(R.id.tv_doctor_one);
        mTv_doctor_two = getActivity().findViewById(R.id.tv_doctor_two);
        mTv_doctor_three = getActivity().findViewById(R.id.tv_doctor_three);
        mVp_doctor_myviewpage = getActivity().findViewById(R.id.vp_doctor_myViewPager);
        mBtnSend = getActivity().findViewById(R.id.send);
        mBtnHistory = getActivity().findViewById(R.id.doctorhistory);
    }

    //添加和删除药物器械运动对象
    public void setmAllMedicineItem (Bean item) {
        mAllMedicineItem.add(item);
    }

    public void deletemAllMedicineItem (int position) {
        mAllMedicineItem.remove(position);
    }

    public void setmAllInstrumentItem (Bean item) {
        mAllInstrumentItem.add(item);
    }

    public void deletemAllInstrumentItem (int position) {
        mAllInstrumentItem.remove(position);
    }

    public void setmAllSportsItem (Bean item) {
        mAllSportsItem.add(item);
    }

    public void deletemAllSportsItem (int position) {
        mAllSportsItem.remove(position);
    }

    //当前添加的总条目数，用于以后判断是否为空计划
    public void ChangemPlannum (int i) {
        mPlannum += i;
        Toast.makeText(getActivity(), mPlannum + "", Toast.LENGTH_SHORT).show();
    }

    //设置一个ViewPager的侦听事件，当左右滑动ViewPager时菜单栏被选中状态跟着改变
    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged (int arg0) {
        }

        @Override
        public void onPageScrolled (int arg0, float arg1, int arg2) {
        }

        //页面选中时的颜色变化
        @Override
        public void onPageSelected (int arg0) {
            switch (arg0) {
                case 0:
                    mTv_doctor_one.setBackgroundColor(Color.LTGRAY);
                    mTv_doctor_two.setBackgroundColor(Color.WHITE);
                    mTv_doctor_three.setBackgroundColor(Color.WHITE);
                    break;
                case 1:
                    mTv_doctor_one.setBackgroundColor(Color.WHITE);
                    mTv_doctor_two.setBackgroundColor(Color.LTGRAY);
                    mTv_doctor_three.setBackgroundColor(Color.WHITE);
                    break;
                case 2:
                    mTv_doctor_one.setBackgroundColor(Color.WHITE);
                    mTv_doctor_two.setBackgroundColor(Color.WHITE);
                    mTv_doctor_three.setBackgroundColor(Color.LTGRAY);
                    break;
            }
        }
    }
}
