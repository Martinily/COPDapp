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
    private View mView;

    private PatientBottomActivity mPatientBottomActivity;

    private TextView mTv_doctor_title, mTv_doctor_username, mTv_doctor_perinfo, mTv_doctor_sign, mTv_doctor_setting;

    private ImageButton mIb_doctor_portrait, mIb_doctor_perinfo, mIb_doctor_sign, mIb_doctor_setting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_doctor_personal_center, container, false);

        initUIComponents();
        addListeners();

        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.mPatientBottomActivity = ((PatientBottomActivity) getActivity());
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
        mTv_doctor_perinfo = mView.findViewById(R.id.tv_doctor_perinfo);
        mTv_doctor_title = mView.findViewById(R.id.tv_doctor_title);
        mTv_doctor_setting = mView.findViewById(R.id.tv_doctor_setting);
        mTv_doctor_username = mView.findViewById(R.id.tv_doctor_username);
        mTv_doctor_sign = mView.findViewById(R.id.tv_doctor_sign);

        mIb_doctor_portrait = mView.findViewById(R.id.ib_doctor_portrait);
        mIb_doctor_perinfo = mView.findViewById(R.id.ib_doctor_perinfo);
        mIb_doctor_setting = mView.findViewById(R.id.ib_doctor_setting);
        mIb_doctor_sign = mView.findViewById(R.id.ib_doctor_sign);
    }

    private void addListeners() {

        //ib_doctor_perinfo.setOnClickListener(e->openNew(DoctorMyInfoActivity.class));
        mIb_doctor_sign.setOnClickListener(e -> openNew(DoctorSignActivity.class));
        mIb_doctor_setting.setOnClickListener(e -> openNew(DoctorSettingActivity.class));
    }

    private void openNew(Class c) {
        //是否更新界面
        Intent intent = new Intent(mPatientBottomActivity, c);

        startActivity(intent);
    }
}
