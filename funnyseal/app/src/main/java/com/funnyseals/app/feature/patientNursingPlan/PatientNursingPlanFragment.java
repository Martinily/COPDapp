package com.funnyseals.app.feature.patientNursingPlan;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.funnyseals.app.R;

import java.util.ArrayList;
import java.util.List;

/**
护理计划to fragment
 */
public class PatientNursingPlanFragment extends Fragment implements View.OnClickListener{
    private View                      mView;
    private TextView                  mTv_patient_one;
    private TextView                  mTv_patient_two;
    private TextView                  mTv_patient_three;
    private ViewPager                 mVp_patient_myViewPager;
    private List<Fragment>            mList;
    private PatientTabFragmentAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_patient_nursing_plan, container, false);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        // 设置菜单栏的点击事件
        mTv_patient_one.setOnClickListener(this);
        mTv_patient_two.setOnClickListener(this);
        mTv_patient_three.setOnClickListener(this);
        mVp_patient_myViewPager.setOnPageChangeListener(new MyPagerChangeListener());

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

    @Override
    public void onClick(View v) {

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
        }
    }

    private void initView() {
        mTv_patient_one = getActivity().findViewById(R.id.tv_patient_one);
        mTv_patient_two = getActivity().findViewById(R.id.tv_patient_two);
        mTv_patient_three = getActivity().findViewById(R.id.tv_patient_three);
        mVp_patient_myViewPager = getActivity().findViewById(R.id.vp_patient_myViewPager);
    }

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
