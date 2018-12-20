package com.funnyseals.app.feature.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.funnyseals.app.R;
import com.funnyseals.app.feature.bottomtab.DoctorBottomActivity;
import com.funnyseals.app.model.User;

import java.util.List;
import java.util.Objects;

/**
 * 患者列表
 */
public class PatientListFragment extends Fragment {
    private View                   mView;
    private SwipeMenuListView      mPatientlist;
    private PatientListItemAdapter mAdapter;
    private List<User>             mUsers;

    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_patient_list, container, false);

        init();

        return mView;
    }

    @Override
    public void onResume () {
        super.onResume();
    }


    private void init () {
        mUsers = ((DoctorBottomActivity) Objects.requireNonNull(getActivity())).getAllMyPatient();
        mPatientlist = mView.findViewById(R.id.patient_list);
        mPatientlist.setAdapter(new PatientListItemAdapter(getActivity(), mUsers));
        addListeners();
    }


    /*
    列表点击事件
     */
    private void addListeners () {
        mPatientlist.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getContext(), UserInfoActivity.class);
            for (User p : mUsers) {
                if (p.getAccount().equals(mUsers.get(position).getAccount())) {
                    intent.putExtra("user", p);
                    break;
                }
            }
            startActivity(intent);
        });
    }

}
