package com.funnyseals.app.feature.patientPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnyseals.app.R;

/**
 * 患者端个人中心to fragment(undo)
 */
public class PatientPersonalCenterFragment extends Fragment {
    private View view;
    private ImageView iv_patient_portrait;
    // private patientButtonActivity patientButtonActivity
    private TextView getTv_patient_usename,getTv_patient_title,getTv_patient_perinfo,getTv_patient_doctor,getTv_patient_equipment,getTv_patient_setting;
    private ImageButton getIb_patient_portrait,getIb_patient_perinfo,getIb_patient_doctor,getIb_patient_equipment,getIb_patient_setting;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_patient_personal_center, container, false);

        initUIComponents();
        addListeners();

        return view;
    }

    //  @Override
    //public void onAttach(Context context) {
    //   super.onAttach(context);

    // this.patientBottomActivity = ((PatientBottomActivity) getActivity());

    //}



    //初始化
    private void initUIComponents() {

        getTv_patient_perinfo=(TextView)view.findViewById(R.id.tv_patient_perinfo);
        getTv_patient_title=(TextView)view.findViewById(R.id.tv_patient_title);
        getTv_patient_setting=(TextView)view.findViewById(R.id.tv_patient_setting);
        getTv_patient_equipment=(TextView)view.findViewById(R.id.tv_patient_equipment)
        getTv_patient_usename=(TextView)view.findViewById(R.id.tv_patient_username);
        getTv_patient_doctor=(TextView)view.findViewById(R.id.tv_patient_doctor);

        getIb_patient_perinfo=(ImageButton)view.findViewById(R.id.ib_patient_perinfo);
        getIb_patient_setting=(ImageButton)view.findViewById(R.id.ib_patient_setting);
        getIb_patient_doctor=(ImageButton)view.findViewById(R.id.ib_patient_doctor);
        getIb_patient_equipment=(ImageButton)view.findViewById(R.id.ib_patient_equipment);
    }

    //监控
    private void addListeners() {





    }

    private void openNew(Class c) {
        // TODO 刷新页面

    }
}