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

/*医生护理计划总页面*/
public class DoctorNursingPlanActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTv_doctor_title, mTv_doctor_one, mTv_doctor_two, mTv_doctor_three;
    private ViewPager                     mVp_doctor_myviewpage;
    private List<Fragment>                mList;
    private DoctorTabFragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_nursing_plan);
        InitView();
        // 设置菜单栏的点击事件
        mTv_doctor_one.setOnClickListener(this);
        mTv_doctor_two.setOnClickListener(this);
        mTv_doctor_three.setOnClickListener(this);
        mVp_doctor_myviewpage.setOnPageChangeListener(new MyPagerChangeListener());

        //把Fragment添加到List集合里面
        mList = new ArrayList<>();
        mList.add(new DoctorOneFragment());
        mList.add(new DoctorTwoFragment());
        mList.add(new DoctorThreeFragment());
        mAdapter = new DoctorTabFragmentPagerAdapter(getSupportFragmentManager(), mList);
        mVp_doctor_myviewpage.setAdapter(mAdapter);
        mVp_doctor_myviewpage.setCurrentItem(0);  //初始化显示第一个页面
        mTv_doctor_one.setBackgroundColor(Color.LTGRAY);//被选中就为灰色
    }

    /**
     * 初始化控件
     */
    private void InitView() {
        mTv_doctor_one = findViewById(R.id.tv_doctor_one);
        mTv_doctor_two = findViewById(R.id.tv_doctor_two);
        mTv_doctor_three = findViewById(R.id.tv_doctor_three);
        mVp_doctor_myviewpage = findViewById(R.id.vp_doctor_myViewPager);
    }

    /**
     * 菜单栏点击事件
     */
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