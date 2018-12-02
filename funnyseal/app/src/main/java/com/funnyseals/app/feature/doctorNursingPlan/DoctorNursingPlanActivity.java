package com.funnyseals.app.feature.doctorNursingPlan;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.funnyseals.app.R;

import java.util.ArrayList;
import java.util.List;

public class DoctorNursingPlanActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_doctor_title, tv_doctor_one, tv_doctor_two, tv_doctor_three;
    private ViewPager                     vp_doctor_myviewpage;
    private List<Fragment>                list;
    private DoctorTabFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_nursing_plan);
        InitView();
        // 设置菜单栏的点击事件
        tv_doctor_one.setOnClickListener(this);
        tv_doctor_two.setOnClickListener(this);
        tv_doctor_three.setOnClickListener(this);
        vp_doctor_myviewpage.setOnPageChangeListener(new MyPagerChangeListener());

        //把Fragment添加到List集合里面
        list = new ArrayList<>();
        list.add(new DoctorOneFragment());
        list.add(new DoctorTwoFragment());
        list.add(new DoctorThreeFragment());
        adapter = new DoctorTabFragmentPagerAdapter(getSupportFragmentManager(), list);
        vp_doctor_myviewpage.setAdapter(adapter);
        vp_doctor_myviewpage.setCurrentItem(0);  //初始化显示第一个页面
        tv_doctor_one.setBackgroundColor(Color.LTGRAY);//被选中就为灰色
    }

    /**
     * 初始化控件
     */
    private void InitView() {
        tv_doctor_one = findViewById(R.id.tv_doctor_one);
        tv_doctor_two = findViewById(R.id.tv_doctor_two);
        tv_doctor_three = findViewById(R.id.tv_doctor_three);
        vp_doctor_myviewpage = findViewById(R.id.vp_doctor_myViewPager);
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_doctor_one:
                vp_doctor_myviewpage.setCurrentItem(0);
                tv_doctor_one.setBackgroundColor(Color.LTGRAY);
                tv_doctor_two.setBackgroundColor(Color.WHITE);
                tv_doctor_three.setBackgroundColor(Color.WHITE);
                break;
            case R.id.tv_doctor_two:
                vp_doctor_myviewpage.setCurrentItem(1);
                tv_doctor_one.setBackgroundColor(Color.WHITE);
                tv_doctor_two.setBackgroundColor(Color.LTGRAY);
                tv_doctor_three.setBackgroundColor(Color.WHITE);
                break;
            case R.id.tv_doctor_three:
                vp_doctor_myviewpage.setCurrentItem(2);
                tv_doctor_one.setBackgroundColor(Color.WHITE);
                tv_doctor_two.setBackgroundColor(Color.WHITE);
                tv_doctor_three.setBackgroundColor(Color.LTGRAY);
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
                    tv_doctor_one.setBackgroundColor(Color.LTGRAY);
                    tv_doctor_two.setBackgroundColor(Color.WHITE);
                    tv_doctor_three.setBackgroundColor(Color.WHITE);
                    break;
                case 1:
                    tv_doctor_one.setBackgroundColor(Color.WHITE);
                    tv_doctor_two.setBackgroundColor(Color.LTGRAY);
                    tv_doctor_three.setBackgroundColor(Color.WHITE);
                    break;
                case 2:
                    tv_doctor_one.setBackgroundColor(Color.WHITE);
                    tv_doctor_two.setBackgroundColor(Color.WHITE);
                    tv_doctor_three.setBackgroundColor(Color.LTGRAY);
                    break;
            }
        }
    }
}
