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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

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

    //显示
    private void showTypeDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        final android.support.v7.app.AlertDialog dialog = builder.create();
        View view = View.inflate(getActivity(), R.layout.person_center_button, null);
        Button tv_select_gallery =  view.findViewById(R.id.btn_take_photo);
        Button tv_select_camera =  view.findViewById(R.id.btn_pick_photo);
        // 在相册中选取
        tv_select_gallery.setOnClickListener(v -> {
            Intent intent1 = new Intent(Intent.ACTION_PICK, null);
            intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent1, 1);
            dialog.dismiss();
        });
        // 调用照相机
        tv_select_camera.setOnClickListener(v -> {
            Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
            startActivityForResult(intent2, 2);// 采用ForResult打开
            dialog.dismiss();
        });
        dialog.setView(view);
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }

                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        /**
                         * 上传服务器代码
                         */
                        setPicToView(head);// 保存在SD卡中
                        iv_patient_portrait.setImageBitmap(head);// 用ImageView显示出来
                    }
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 调用系统的裁剪功能
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

        Bitmap bt = BitmapFactory.decodeFile(path+"head.jpg");//从sd卡找，转化为bitmap
        if(bt!=null){
            Drawable drawable = new BitmapDrawable(bt);//转化为drawable
            iv_patient_portrait.setImageDrawable(drawable);
        }
        else {
            /*
            服务器代码
            */
        }
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
                 //   startActivity(new Intent(getActivity(),Pa));
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