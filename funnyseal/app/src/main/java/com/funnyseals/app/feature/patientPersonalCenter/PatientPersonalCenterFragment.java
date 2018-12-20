package com.funnyseals.app.feature.patientPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.funnyseals.app.R;

/**
 * 患者端个人中心to fragment(undo)
 */
@SuppressWarnings("deprecation")
public class PatientPersonalCenterFragment extends Fragment {
    private View        mView;
    private ImageView   iv_patient_portrait;
    private ImageButton getIb_patient_perinfo, getIb_patient_doctor, getIb_patient_equipment,
            getIb_patient_setting;

    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_patient_personal_center, container, false);

        initUIComponents();
        return mView;
    }

    /**
     * 初始化控件
     */
    private void initUIComponents () {
        getIb_patient_perinfo = mView.findViewById(R.id.ib_patient_perinfo);
        getIb_patient_perinfo.setOnClickListener(new addListeners());
        getIb_patient_setting = mView.findViewById(R.id.ib_patient_setting);
        getIb_patient_setting.setOnClickListener(new addListeners());
        getIb_patient_doctor = mView.findViewById(R.id.ib_patient_doctor);
        getIb_patient_doctor.setOnClickListener(new addListeners());
        getIb_patient_equipment = mView.findViewById(R.id.ib_patient_equipment);
        getIb_patient_equipment.setOnClickListener(new addListeners());
    }

    /**
     * 监听事件
     * 跳转 我的医生
     * 个人信息
     * 我的设备
     * 设置图片
     */
    private class addListeners implements View.OnClickListener {
        @Override
        public void onClick (View v) {
            switch (v.getId()) {
                case R.id.ib_patient_perinfo:
                    startActivity(new Intent(getActivity(), PatientMyInfoModifyActivity.class));
                    break;
                case R.id.ib_patient_doctor:
                    startActivity(new Intent(getActivity(), PatientMyDoctorActivity.class));
                    break;
                case R.id.ib_patient_setting:
                    startActivity(new Intent(getActivity(), PatientSetting.class));
                    break;
                case R.id.ib_patient_equipment:
                    startActivity(new Intent(getActivity(), PatientMyEquipmentActivity.class));
                    break;
                default:
                    break;
            }
        }
    }
}