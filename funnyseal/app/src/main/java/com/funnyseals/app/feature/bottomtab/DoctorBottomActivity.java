package com.funnyseals.app.feature.bottomtab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toolbar;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.list.PatientListFragment;
import com.funnyseals.app.feature.doctorMessage.DoctorMessageListFragment;
import com.funnyseals.app.feature.doctorNursingPlan.DoctorNursingPlanFragment;
import com.funnyseals.app.feature.doctorPersonalCenter.DoctorPersonalCenterFragment;

public class DoctorBottomActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private RadioButton indexTab;
    //之前的界面id
    private int         previousTabId;

    private DoctorBottomTabAdapter fragmentTabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_bottom);
        //初始化导航栏
        initBottomTabs();
    }

    //返回
    @Override
    public void onBackPressed() {
        if (indexTab.isChecked()) {
            super.onBackPressed();
        } else {
            indexTab.setChecked(true);
        }
    }

    //恢复
    @Override
    protected void onResume() {
        super.onResume();

        showPrevious();
    }

    private void initBottomTabs() {
        indexTab = findViewById(R.id.doctor_nursingPlan_tab);
        previousTabId = R.id.doctor_nursingPlan_tab;

        RadioGroup bottomTabs = findViewById(R.id.doctor_bottom_tabs);

        SparseArray<Fragment> fragmentMap = new SparseArray<>();

        fragmentMap.put(R.id.doctor_patientList_tab, new PatientListFragment());
        fragmentMap.put(R.id.doctor_message_tab, new DoctorMessageListFragment());
        fragmentMap.put(R.id.doctor_nursingPlan_tab, new DoctorNursingPlanFragment());
        fragmentMap.put(R.id.doctor_personalCenter_tab, new DoctorPersonalCenterFragment());

        fragmentTabAdapter = new BottomTabBar(this, fragmentMap, R.id.fl_doctor_content, bottomTabs);
    }

    public void showFragmentTab(int tabID) {
        fragmentTabAdapter.setFragmentTab(tabID);
    }

    public void showPrevious() {
        showFragmentTab(previousTabId);
    }

    private class BottomTabBar extends DoctorBottomTabAdapter {

        BottomTabBar(FragmentActivity fragmentActivity, SparseArray<Fragment> fragmentMap, int fragmentContentId, RadioGroup radioGroup) {
            super(fragmentActivity, fragmentMap, fragmentContentId, radioGroup);
        }

        @Override
        public boolean onTabWillChange(int tabId) {

            previousTabId = tabId;
            return true;
        }

        @Override
        public void onTabChanged(int tabId) {

        }
    }
}
