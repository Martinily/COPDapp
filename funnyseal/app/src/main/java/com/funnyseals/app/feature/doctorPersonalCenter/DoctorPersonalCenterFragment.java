package com.funnyseals.app.feature.doctorPersonalCenter;

import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
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
 * 个人中心
 */
public class DoctorPersonalCenterFragment extends Fragment {
    private View mView;
    private PatientBottomActivity mPatientBottomActivity;
    private TextView mTv_doctor_title, mTv_doctor_username, mTv_doctor_perinfo, mTv_doctor_sign, mTv_doctor_setting;
    private ImageButton mIb_doctor_portrait, mIb_doctor_perinfo, mIb_doctor_sign, mIb_doctor_setting;
    private Intent intent_cportrait,intent_myinfo,intent_signing,intent_setting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_doctor_personal_center, container, false);
        initUIComponents();

        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mPatientBottomActivity = ((PatientBottomActivity) getActivity());
    }



    private void initUIComponents() {
        mTv_doctor_perinfo = mView.findViewById(R.id.tv_doctor_perinfo);
        mTv_doctor_title = mView.findViewById(R.id.tv_doctor_title);
        mTv_doctor_setting = mView.findViewById(R.id.tv_doctor_setting);
        mTv_doctor_username = mView.findViewById(R.id.tv_doctor_username);
        mTv_doctor_sign = mView.findViewById(R.id.tv_doctor_sign);

        mIb_doctor_portrait = mView.findViewById(R.id.ib_doctor_portrait);
        mIb_doctor_portrait.setOnClickListener(new addListeners());
        mIb_doctor_perinfo = mView.findViewById(R.id.ib_doctor_perinfo);
        mIb_doctor_perinfo.setOnClickListener(new addListeners());
        mIb_doctor_setting = mView.findViewById(R.id.ib_doctor_setting);
        mIb_doctor_setting.setOnClickListener(new addListeners());
        mIb_doctor_sign = mView.findViewById(R.id.ib_doctor_sign);
        mIb_doctor_sign .setOnClickListener(new addListeners());
    }

  private class addListeners implements View.OnClickListener{

      @Override
      public void onClick(View v) {
          switch (v.getId()){
              case R.id.ib_doctor_portrait:
                  intent_cportrait = new Intent(DoctorPersonalCenterFragment.this,DoctorChangePortraitAcitivity.class);
                  startActivity(intent_cportrait);
                  break;
              case  R.id.ib_doctor_perinfo:
                  intent_myinfo = new Intent(DoctorPersonalCenterFragment.this,DoctorMyInfoActivity.class);
                  startActivity(intent_myinfo);
                  break;
              case R.id.ib_doctor_setting:
                  intent_setting = new Intent(DoctorPersonalCenterFragment.this,DoctorSettingActivity.class);
                  startActivity(intent_setting);
                  break;
              case  R.id.ib_doctor_sign:
                  intent_signing = new Intent(DoctorPersonalCenterFragment.this,DoctorSigningActivity.class);
                  break;
                  default:
                      break;
          }
      }
  }

}
