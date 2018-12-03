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
import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.model.User;
import com.funnyseals.app.model.UserTemp;

import java.util.ArrayList;
import java.util.List;

/**
患者列表
 */
public class PatientListFragment extends Fragment {
    private View                   mView;
    private SwipeMenuListView      mPatientlist;
    private PatientListItemAdapter mAdapter;
    private List<User>             mUsers;
    private List<UserTemp>         mUserTemps;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_patient_list, container, false);

        init();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void init() {
        mPatientlist = mView.findViewById(R.id.patient_list);
        mUsers = getAllUser(((MyApplication) getContext().getApplicationContext()).getUser().getAccount());
        mUserTemps = new ArrayList<>();
        for (User p : mUsers) {
            mUserTemps.add(new UserTemp(R.mipmap.portrait0, p.getNickName(), p.getAccount(), p));
        }
        mPatientlist.setAdapter(new PatientListItemAdapterTemp(getActivity(), mUserTemps));
        addListeners();
    }


    /**
     * 获取所有用户，数据库获取，index为账号
     */
    public List<User> getAllUser(String account) {
        List<User> users = new ArrayList<>();
        //users.add(用户)；
        return users;
    }

    /*
    列表点击事件
     */
    private void addListeners() {
        mPatientlist.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getContext(), UserInfoActivity.class);
            for (User p : mUsers) {
                if (p.getAccount().equals(mUserTemps.get(position).getAccount())) {
                    intent.putExtra("user", p);
                    break;
                }
            }
            startActivity(intent);
        });
    }

}
