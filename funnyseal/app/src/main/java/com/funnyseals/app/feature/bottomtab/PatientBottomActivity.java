package com.funnyseals.app.feature.bottomtab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.health.HealthCenterFragment;
import com.funnyseals.app.feature.patientMessage.PatientMessageListFragment;
import com.funnyseals.app.feature.patientNursingPlan.PatientNursingPlanFragment;
import com.funnyseals.app.feature.patientPersonalCenter.PatientPersonalCenterFragment;

public class PatientBottomActivity extends AppCompatActivity {

    private RadioButton mIndexTab;

    private int mPreviousTabId;

    private PatientBottomTabAdapter mFragmentTabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_bottom);

        //初始化导航栏
        initBottomTabs();
    }

    @Override
    public void onBackPressed() {
        if (mIndexTab.isChecked()) {
            super.onBackPressed();
        } else {
            mIndexTab.setChecked(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        showPrevious();
    }

    private void initBottomTabs() {
        mIndexTab = findViewById(R.id.patient_nursingplan_tab);
        mPreviousTabId = R.id.patient_nursingplan_tab;

        RadioGroup bottomTabs = findViewById(R.id.patient_bottom_tabs);

        SparseArray<Fragment> fragmentMap = new SparseArray<>();
        fragmentMap.put(R.id.patient_nursingplan_tab, new PatientNursingPlanFragment());
        fragmentMap.put(R.id.patient_message_tab, new PatientMessageListFragment());
        fragmentMap.put(R.id.patient_health_tab, new HealthCenterFragment());
        fragmentMap.put(R.id.patient_personalcenter_tab, new PatientPersonalCenterFragment());

        mFragmentTabAdapter = new BottomTabBar(this, fragmentMap, R.id.fl_patient_content, bottomTabs);
    }

    public void showFragmentTab(int tabID) {
        mFragmentTabAdapter.setFragmentTab(tabID);
    }

    public void showPrevious() {
        showFragmentTab(mPreviousTabId);
    }

    private class BottomTabBar extends PatientBottomTabAdapter {

        BottomTabBar(FragmentActivity fragmentActivity, SparseArray<Fragment> fragmentMap, int fragmentContentId, RadioGroup radioGroup) {
            super(fragmentActivity, fragmentMap, fragmentContentId, radioGroup);
        }

        @Override
        public boolean onTabWillChange(int tabId) {

            mPreviousTabId = tabId;
            return true;
        }

        @Override
        public void onTabChanged(int tabId) {

        }
    }
}
