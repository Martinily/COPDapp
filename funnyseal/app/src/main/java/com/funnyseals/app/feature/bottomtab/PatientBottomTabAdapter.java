package com.funnyseals.app.feature.bottomtab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.funnyseals.app.R;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/11/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public abstract class PatientBottomTabAdapter implements RadioGroup.OnCheckedChangeListener {
    //所有可用fragment列表
    private SparseArray<Fragment> mFragmentMap;
    //底部导航栏
    private RadioGroup            mRadioGroup;

    private FragmentActivity mFragmentActivity;
    //容器组件id
    private int              mFragmentContentId;
    //当前tab对应的RadioButton id
    private int              mCurrentTabId;

    protected PatientBottomTabAdapter (FragmentActivity fragmentActivity, SparseArray<Fragment>
            fragmentMap, int fragmentContentId, RadioGroup radioGroup) {
        this.mFragmentMap = fragmentMap;
        this.mRadioGroup = radioGroup;
        this.mFragmentActivity = fragmentActivity;
        this.mFragmentContentId = fragmentContentId;

        this.init();
    }

    private void init () {

        FragmentTransaction ft = mFragmentActivity.getSupportFragmentManager().beginTransaction();
        ft.add(mFragmentContentId, mFragmentMap.get(R.id.patient_personalcenter_tab));
        ft.commit();

        mRadioGroup.setOnCheckedChangeListener(this);

        mCurrentTabId = R.id.patient_personalcenter_tab;
    }

    @Override
    public void onCheckedChanged (RadioGroup radioGroup, int checkedId) {

        changeTab(checkedId);
    }

    /**
     * 切换 fragment tab
     * <p>
     * 切换前调用 #onTabWillChange
     * 切换后调用 #onTabChanged
     *
     * @param tabId 目标 tab 对应的 RadioButton id
     */
    private void changeTab (int tabId) {
        if (tabId == mCurrentTabId) {
            return;
        }

        if (onTabWillChange(tabId)) {

            showTab(tabId);

            onTabChanged(tabId);
        }
    }

    /**
     * 显示 fragment tab
     *
     * @param tabId 目标tab 对应的 RadioButton id
     */
    private void showTab (int tabId) {

        Fragment temp = getCurrentFragment();

        Fragment fragment = mFragmentMap.get(tabId);

        FragmentTransaction ft = mFragmentActivity.getSupportFragmentManager().beginTransaction();

        if (fragment.isAdded()) {
            fragment.onResume();
        } else {
            ft.add(mFragmentContentId, fragment);
        }

        temp.onPause();

        ft.show(mFragmentMap.get(tabId));

        ft.hide(temp);

        ft.commit();

        mCurrentTabId = tabId;
    }

    /**
     * 获取当前tab
     */
    private Fragment getCurrentFragment () {
        return mFragmentMap.get(mCurrentTabId);
    }

    /**
     * 手动切换 fragment tab
     *
     * @param tabId 目标 tab RadioButton id
     */
    public void setFragmentTab (int tabId) {
        changeTab(tabId);

        if (mFragmentActivity.findViewById(tabId) instanceof RadioButton) {
            ((RadioButton) mFragmentActivity.findViewById(tabId)).setChecked(true);
        }
    }

    /**
     * fragment tab 切换前的回调函数
     *
     * @param tabId 目标tab 对应的 RadioButton id
     * @return 确定切换返回true，否则返回false
     */
    public abstract boolean onTabWillChange (int tabId);

    /**
     * fragment tab切换后的回调函数
     *
     * @param tabId 目标tab 对应的 RadioButton id
     */
    public abstract void onTabChanged (int tabId);
}
