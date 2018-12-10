package com.funnyseals.app.feature.doctorNursingPlan;

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

import com.funnyseals.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 医生端护理计划fragment
 */
public class DoctorNursingPlanFragment extends Fragment implements View.OnClickListener {
    private TextView   mTv_doctor_one;
    private TextView   mTv_doctor_two;
    private TextView   mTv_doctor_three;
    private ViewPager  mVp_doctor_myviewpage;
    private Button     mBtnSend;
    private String     planID;
    private List<Bean> mAllItem;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_doctor_nursing_plan, null);
        mAllItem = new ArrayList<>();
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        // 设置菜单栏的点击事件
        mTv_doctor_one.setOnClickListener(this);
        mTv_doctor_two.setOnClickListener(this);
        mTv_doctor_three.setOnClickListener(this);
        mVp_doctor_myviewpage.setOnPageChangeListener(new MyPagerChangeListener());
        mBtnSend.setOnClickListener(this);

        //把Fragment添加到List集合里面
        List<Fragment> mList = new ArrayList<>();
        mList.add(new DoctorOneFragment());
        mList.add(new DoctorTwoFragment());
        mList.add(new DoctorThreeFragment());
        DoctorTabFragmentPagerAdapter mAdapter = new DoctorTabFragmentPagerAdapter(getChildFragmentManager(), mList);
        mVp_doctor_myviewpage.setAdapter(mAdapter);
        mVp_doctor_myviewpage.setCurrentItem(0);  //初始化显示第一个页面
        mTv_doctor_one.setBackgroundColor(Color.LTGRAY);//被选中就为灰色
    }

    @Override
    public void onClick(View v) {

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
            case R.id.send:
                for (Bean p : mAllItem) {
                    System.err.println(p.getName());
                }
                break;
        }
    }

    private void initView() {
        mTv_doctor_one = getActivity().findViewById(R.id.tv_doctor_one);
        mTv_doctor_two = getActivity().findViewById(R.id.tv_doctor_two);
        mTv_doctor_three = getActivity().findViewById(R.id.tv_doctor_three);
        mVp_doctor_myviewpage = getActivity().findViewById(R.id.vp_doctor_myViewPager);
        mBtnSend = getActivity().findViewById(R.id.send);
    }

    /**
     * 设置一个ViewPager的侦听事件，当左右滑动ViewPager时菜单栏被选中状态跟着改变
     */
    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
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

    public void setAllItem(Bean item) {
        mAllItem.add(item);
    }
}
