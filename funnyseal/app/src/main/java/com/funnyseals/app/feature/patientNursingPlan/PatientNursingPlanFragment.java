package com.funnyseals.app.feature.patientNursingPlan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funnyseals.app.R;

import java.util.Objects;

/**

 */
public class PatientNursingPlanFragment extends Fragment {
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_patient_nursing_plan, container, false);

        startActivity(new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), PatientNursingPlanActivity.class));
        return mView;
    }
}
