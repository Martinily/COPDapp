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
 *     time   : 2018/11/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public abstract class DoctorBottomTabAdapter implements RadioGroup.OnCheckedChangeListener {
    //所有可用fragment列表
    private SparseArray<Fragment> fragmentMap;
    //底部导航栏
    private RadioGroup            radioGroup;
    //
    private FragmentActivity      fragmentActivity;
    //容器组件id
    private int                   fragmentContentId;
    //当前tab对应的RadioButton id
    private int                   currentTabId;


    DoctorBottomTabAdapter(FragmentActivity fragmentActivity, SparseArray<Fragment> fragmentMap, int fragmentContentId, RadioGroup radioGroup) {
        this.fragmentMap = fragmentMap;
        this.radioGroup = radioGroup;
        this.fragmentActivity = fragmentActivity;
        this.fragmentContentId = fragmentContentId;

        this.init();
    }

    private void init() {
        // 初始状态装载 doctor_nursingPlan_tab
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        ft.add(fragmentContentId, fragmentMap.get(R.id.doctor_nursingPlan_tab));
        ft.commit();

        radioGroup.setOnCheckedChangeListener(this);

        currentTabId = R.id.doctor_nursingPlan_tab;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        // 切换
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
    private void changeTab(int tabId) {
        if (tabId == currentTabId) { // 选中当前tab无需切换
            return;
        }
        // 调用注册的回调函数
        if (onTabWillChange(tabId)) {
            // 显示选中的tab
            showTab(tabId);
            // 调用注册的回调函数
            onTabChanged(tabId);
        }
    }

    /**
     * 显示 fragment tab
     *
     * @param tabId 目标tab 对应的 RadioButton id
     */
    private void showTab(int tabId) {
        // 当前 fragment
        Fragment temp = getCurrentFragment();
        // 目标 fragment
        Fragment fragment = fragmentMap.get(tabId);

        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();

        if (fragment.isAdded()) { // 已添加
            fragment.onResume();
        } else {
            ft.add(fragmentContentId, fragment);
        }
        // 暂停
        temp.onPause();
        // 显示目标 fragment
        ft.show(fragmentMap.get(tabId));
        // 隐藏原 fragment
        ft.hide(temp);

        ft.commit();

        // 修改当前 fragment tab
        currentTabId = tabId;
    }

    /**
     * 获取当前tab
     */
    private Fragment getCurrentFragment() {
        return fragmentMap.get(currentTabId);
    }

    /**
     * 手动切换 fragment tab
     *
     * @param tabId 目标 tab RadioButton id
     */
    public void setFragmentTab(int tabId) {
        changeTab(tabId);

        if (fragmentActivity.findViewById(tabId) instanceof RadioButton) {
            ((RadioButton) fragmentActivity.findViewById(tabId)).setChecked(true);
        }
    }

    /**
     * fragment tab 切换前的回调函数
     *
     * @param tabId 目标tab 对应的 RadioButton id
     * @return 确定切换返回true，否则返回false
     */
    public abstract boolean onTabWillChange(int tabId);

    /**
     * fragment tab切换后的回调函数
     *
     * @param tabId 目标tab 对应的 RadioButton id
     */
    public abstract void onTabChanged(int tabId);
}
