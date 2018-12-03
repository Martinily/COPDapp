package com.funnyseals.app.feature.doctorNursingPlan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 医生端护理计划fragment
 */
public class DoctorNursingPlanFragment extends Fragment {
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        startActivity(new Intent(getContext(), DoctorNursingPlanActivity.class));
        return mView;
    }
}
