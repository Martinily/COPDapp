package com.funnyseals.app.feature.patientNursingPlan;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/11/30
 *     desc   :fragment for patient
 *     version: 1.0
 * </pre>
 */
public class PatientTabFragmentAdapter extends FragmentPagerAdapter {
    private FragmentManager mFragmentManager;
    private List<Fragment>  mList;

    public PatientTabFragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mList = list;
        this.mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int arg0) {
        return mList.get(arg0);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }
}
