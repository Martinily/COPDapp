package com.funnyseals.app.feature.doctorPersonalCenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.bottomtab.PatientBottomActivity;

/**
 */
public class DoctorPersonalCenterFragment extends Fragment {
    private View view;

    private PatientBottomActivity patientBottomActivity;

    private TextView tv_doctor_title,tv_doctor_username,tv_doctor_perinfo,tv_doctor_sign,tv_doctor_setting;

    private ImageButton ib_doctor_portrait,ib_doctor_perinfo,ib_doctor_sign,ib_doctor_setting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_doctor_personal_center, container, false);

        initUIComponents();
        addListeners();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.patientBottomActivity = ((PatientBottomActivity) getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (requestCode == EDIT_CODE && resultCode == EditInfoActivity.RESULT_CODE) {
            showData((UserInfo) data.getSerializableExtra("userInfo"));
        }*/
    }

    private void initUIComponents() {
        tv_doctor_perinfo=view.findViewById(R.id.tv_doctor_perinfo);
        tv_doctor_title=view.findViewById(R.id.tv_doctor_title);
        tv_doctor_setting=view.findViewById(R.id.tv_doctor_setting);
        tv_doctor_username=view.findViewById(R.id.tv_doctor_username);
        tv_doctor_sign=view.findViewById(R.id.tv_doctor_sign);

        ib_doctor_portrait=view.findViewById(R.id.ib_doctor_portrait);
        ib_doctor_perinfo=view.findViewById(R.id.ib_doctor_perinfo);
        ib_doctor_setting=view.findViewById(R.id.ib_doctor_setting);
        ib_doctor_sign=view.findViewById(R.id.ib_doctor_sign);
    }

    private void addListeners() {

        //ib_doctor_perinfo.setOnClickListener(e->openNew(DoctorMyInfoActivity.class));
        ib_doctor_sign.setOnClickListener(e->openNew(DoctorSignActivity.class));
        ib_doctor_setting.setOnClickListener(e->openNew(DoctorSettingActivity.class));
    }

    private void openNew(Class c) {
        // TODO 是否更新界面
        Intent intent = new Intent(patientBottomActivity, c);

        startActivity(intent);
    }
}
