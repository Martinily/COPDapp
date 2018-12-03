package com.funnyseals.app.feature.patientNursingPlan;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.funnyseals.app.R;

import java.util.ArrayList;
import java.util.List;

/*
患者端护理计划页面
 */
public class PatientNursingPlanActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView                  mTv_patient_one;
    private TextView                  mTv_patient_two;
    private TextView                  mTv_patient_three;
    private ViewPager                 mVp_patient_myViewPager;
    private List<Fragment>            mList;
    private PatientTabFragmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_nursing_plan);

        InitView();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
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
        mAdapter = new PatientTabFragmentAdapter(getSupportFragmentManager(), mList);
        mVp_patient_myViewPager.setAdapter(mAdapter);
        mVp_patient_myViewPager.setCurrentItem(0);  //初始化显示第一个页面
        mTv_patient_one.setBackgroundColor(Color.LTGRAY);//被选中就为灰色
    }

    /**
     * 初始化控件
     */
    private void InitView() {
        mTv_patient_one = findViewById(R.id.tv_patient_one);
        mTv_patient_two = findViewById(R.id.tv_patient_two);
        mTv_patient_three = findViewById(R.id.tv_patient_three);
        mVp_patient_myViewPager = findViewById(R.id.vp_patient_myViewPager);
    }

    /**
     * 点击事件
     */
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
