package com.funnyseals.app.feature.patientPersonalCenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.feature.doctorNursingPlan.DoctorNursingPlanActivity;
import com.funnyseals.app.feature.patientNursingPlan.MedicineRetimeActivity;
import com.funnyseals.app.model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.mob.tools.utils.DeviceHelper.getApplication;

/**
 * 患者端个人中心to fragment(undo)
 */
@SuppressWarnings("deprecation")
public class PatientPersonalCenterFragment extends Fragment {
    private Context mContext;
    private View mView;
    private ImageView iv_patient_portrait;
    private ImageButton getIb_patient_perinfo,getIb_patient_doctor,getIb_patient_equipment,getIb_patient_setting;
    private Bitmap head;//头像
    private static String path="/sdcard/myHead";//照片路径
    private MyApplication myApplication;

    /*
    监听
    跳转我的医生
        个人信息
        我的设备
        设置图片
     */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_patient_personal_center, container, false);
        MyApplication application = (MyApplication) getActivity().getApplication();

        initUIComponents();
        return mView;
    }

    //初始化
    private void initUIComponents() {

        iv_patient_portrait=mView.findViewById(R.id.iv_patient_portrait);
        iv_patient_portrait.setOnClickListener(new addListeners());
        getIb_patient_perinfo=mView.findViewById(R.id.ib_patient_perinfo);
        getIb_patient_perinfo.setOnClickListener(new addListeners());
        getIb_patient_setting=mView.findViewById(R.id.ib_patient_setting);
        getIb_patient_setting.setOnClickListener( new addListeners());
        getIb_patient_doctor=mView.findViewById(R.id.ib_patient_doctor);
        getIb_patient_doctor.setOnClickListener( new addListeners());
        getIb_patient_equipment=mView.findViewById(R.id.ib_patient_equipment);
        getIb_patient_equipment.setOnClickListener(new addListeners());
    }
    //监听
    private  class addListeners  implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ib_patient_perinfo:
                    startActivity(new Intent(getActivity(),PatientMyInfoModifyActivity.class));
                    break;
                case  R.id.ib_patient_doctor:
                    startActivity(new Intent(getActivity(),PatientMyDoctorActivity.class));
                    break;
                case R.id.ib_patient_setting:
                    startActivity(new Intent(getActivity(),PatientSetting.class));
                    break;
                case R.id.ib_patient_equipment:
                    startActivity(new Intent(getActivity(),PatientMyEquipmentActivity.class));
                    break;
                default:
                    break;
            }
        }
    }
}