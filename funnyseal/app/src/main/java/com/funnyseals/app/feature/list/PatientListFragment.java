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

 */
public class PatientListFragment extends Fragment {
    private View                   view;
    private SwipeMenuListView      patientlist;
    private PatientListItemAdapter adapter;
    private List<User>             users;
    private List<UserTemp>         userTemps;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_list, container, false);

        init();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void init() {
        patientlist = view.findViewById(R.id.patient_list);
        users=getAllUser(((MyApplication) getContext().getApplicationContext()).getUser().getAccount());
        userTemps=new ArrayList<>();
        for(User p:users){
            userTemps.add(new UserTemp(R.mipmap.portrait0, p.getNickName(),p.getAccount(),p));
        }
        patientlist.setAdapter(new PatientListItemAdapterTemp(getActivity(), userTemps));
        addListeners();
    }


    //获取所有用户，数据库获取，index为账号
    public List<User> getAllUser(String account){
        List<User> users=new ArrayList<>();
        //users.add(用户)；
        return users;
    }

    private void addListeners(){
        patientlist.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getContext(), UserInfoActivity.class);
            for(User p:users){
                if(p.getAccount().equals(userTemps.get(position).getAccount())){
                    intent.putExtra("user", p);
                    break;
                }
            }
            startActivity(intent);
        });
    }

}
