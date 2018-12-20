package com.funnyseals.app.feature.doctorPersonalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.funnyseals.app.R;

/**
 * 医生端
 * 个人中心界面 fragment
 */
@SuppressWarnings("deprecation")
public class DoctorPersonalCenterFragment extends Fragment {
    private View        mView;
    private ImageButton ib_doctor_perinfo, ib_doctor_sign, ib_doctor_setting;

    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_doctor_personal_center, container, false);
        initUIComponents();
        return mView;
    }

    /*
     *初始化控件
     */
    private void initUIComponents () {
        ib_doctor_perinfo = mView.findViewById(R.id.ib_doctor_perinfo);
        ib_doctor_perinfo.setOnClickListener(new addListeners());
        ib_doctor_setting = mView.findViewById(R.id.ib_doctor_setting);
        ib_doctor_setting.setOnClickListener(new addListeners());
        ib_doctor_sign = mView.findViewById(R.id.ib_doctor_sign);
        ib_doctor_sign.setOnClickListener(new addListeners());
    }

    /*
     * 监听按钮
     * 跳转 个人信息
     *      签约
     *      设置
     */
    private class addListeners implements View.OnClickListener {
        @Override
        public void onClick (View v) {
            switch (v.getId()) {
                case R.id.ib_doctor_perinfo:
                    startActivity(new Intent(getActivity(), DoctorMyInfoModifyActivity.class));
                    break;
                case R.id.ib_doctor_sign:
                    startActivity(new Intent(getActivity(), DoctorSigningActivity.class));
                    break;
                case R.id.ib_patient_setting:
                    //   startActivity(new Intent(getActivity(),Pa));
                    break;
                default:
                    break;
            }
        }
    }
}