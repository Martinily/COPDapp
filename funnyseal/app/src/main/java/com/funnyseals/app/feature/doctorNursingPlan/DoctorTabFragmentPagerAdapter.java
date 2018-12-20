package com.funnyseals.app.feature.doctorNursingPlan;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * time   : 2018/11/30
 * desc   :fragment'sets
 */
public class DoctorTabFragmentPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager mFragmentManager;
    private List<Fragment>  mList;

    public DoctorTabFragmentPagerAdapter (FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mList = list;
    }

    @Override
    public Fragment getItem (int arg0) {
        return mList.get(arg0);//显示第几个页面
    }

    @Override
    public int getCount () {
        return mList.size();//有几个页面
    }
}
