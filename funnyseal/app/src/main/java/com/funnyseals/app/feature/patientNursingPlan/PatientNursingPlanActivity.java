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

public class PatientNursingPlanActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView                  tv_patient_one;
    private TextView                  tv_patient_two;
    private TextView                  tv_patient_three;
    private ViewPager                 vp_patient_myViewPager;
    private List<Fragment>            list;
    private PatientTabFragmentAdapter adapter;

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
        tv_patient_one.setOnClickListener(this);
        tv_patient_two.setOnClickListener(this);
        tv_patient_three.setOnClickListener(this);
        vp_patient_myViewPager.setOnPageChangeListener(new MyPagerChangeListener());

        //把Fragment添加到List集合里面
        list = new ArrayList<>();
        list.add(new PatientOneFragment());
        list.add(new PatientTwoFragment());
        list.add(new PatientThreeFragment());
        adapter = new PatientTabFragmentAdapter(getSupportFragmentManager(), list);
        vp_patient_myViewPager.setAdapter(adapter);
        vp_patient_myViewPager.setCurrentItem(0);  //初始化显示第一个页面
        tv_patient_one.setBackgroundColor(Color.LTGRAY);//被选中就为灰色
    }

    /**
     * 初始化控件
     */
    private void InitView() {
        tv_patient_one = findViewById(R.id.tv_patient_one);
        tv_patient_two = findViewById(R.id.tv_patient_two);
        tv_patient_three = findViewById(R.id.tv_patient_three);
        vp_patient_myViewPager = findViewById(R.id.vp_patient_myViewPager);
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_patient_one:
                vp_patient_myViewPager.setCurrentItem(0);
                tv_patient_one.setBackgroundColor(Color.LTGRAY);
                tv_patient_two.setBackgroundColor(Color.WHITE);
                tv_patient_three.setBackgroundColor(Color.WHITE);
                break;
            case R.id.tv_patient_two:
                vp_patient_myViewPager.setCurrentItem(1);
                tv_patient_one.setBackgroundColor(Color.WHITE);
                tv_patient_two.setBackgroundColor(Color.LTGRAY);
                tv_patient_three.setBackgroundColor(Color.WHITE);
                break;
            case R.id.tv_patient_three:
                vp_patient_myViewPager.setCurrentItem(2);
                tv_patient_one.setBackgroundColor(Color.WHITE);
                tv_patient_two.setBackgroundColor(Color.WHITE);
                tv_patient_three.setBackgroundColor(Color.LTGRAY);
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
                    tv_patient_one.setBackgroundColor(Color.LTGRAY);
                    tv_patient_two.setBackgroundColor(Color.WHITE);
                    tv_patient_three.setBackgroundColor(Color.WHITE);
                    break;
                case 1:
                    tv_patient_one.setBackgroundColor(Color.WHITE);
                    tv_patient_two.setBackgroundColor(Color.LTGRAY);
                    tv_patient_three.setBackgroundColor(Color.WHITE);
                    break;
                case 2:
                    tv_patient_one.setBackgroundColor(Color.WHITE);
                    tv_patient_two.setBackgroundColor(Color.WHITE);
                    tv_patient_three.setBackgroundColor(Color.LTGRAY);
                    break;
            }
        }
    }
}
