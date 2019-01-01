package com.funnyseals.app.feature.patientPersonalCenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.funnyseals.app.R;
import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.feature.doctorPersonalCenter.DoctorPasswordActivity;
import com.funnyseals.app.model.User;
import com.funnyseals.app.util.SocketUtil;
import com.lljjcoder.citypickerview.widget.CityPicker;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 患者端
 * 个人信息界面
 */
public class PatientMyInfoModifyActivity extends AppCompatActivity {

    private EditText ed_patient_modify_myname, ed_patient_modify_mysex, ed_patient_modify_myage;
    private TextView tv_patient_info_account, tv_patient_modify_mysettlingtime,  ed_patient_modify_location;
    private Button bt_patient_modify_complete;
    private String et1, et2, et3, et4;
    private ImageButton ib_patient_modify_return, ib_patient_modify_password,
            ib_patient_modify_advice;
    private User          myUser;
    private MyApplication myApplication;
    private CityPicker cityPicker;
    private String     result;

    /**
     * 调用本地
     * 工具类USER
     */
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_my_info_modify);
        myApplication = (MyApplication) getApplication();
        myUser = myApplication.getUser();
        init();

    }

    /**
     * 初始化控件
     */
    public void init () {
        ed_patient_modify_myname = findViewById(R.id.ed_patient_modify_myname);
        ed_patient_modify_mysex = findViewById(R.id.ed_patient_modify_mysex);
        ed_patient_modify_myage = findViewById(R.id.ed_patient_modify_myage);
        ed_patient_modify_location = findViewById(R.id.ed_patient_modify_location);
        tv_patient_info_account = findViewById(R.id.tv_patient_info_account);
        tv_patient_modify_mysettlingtime = findViewById(R.id.tv_patient_modify_mysettlingtime);

        ed_patient_modify_myname.setText(myUser.getName());
        ed_patient_modify_myage.setText(String.valueOf(myUser.getAge()));
        ed_patient_modify_mysex.setText(myUser.getSex());
        ed_patient_modify_location.setText(myUser.getAddress());
        ed_patient_modify_location.setOnClickListener(new addListeners());
        tv_patient_info_account.setText(myUser.getAccount());
        tv_patient_modify_mysettlingtime.setText(myUser.getRegisterTime());

        bt_patient_modify_complete = findViewById(R.id.bt_patient_modify_complete);
        bt_patient_modify_complete.setOnClickListener(new addListeners());
        ib_patient_modify_return = findViewById(R.id.ib_patient_modify_return);
        ib_patient_modify_return.setOnClickListener(new addListeners());
        ib_patient_modify_password = findViewById(R.id.ib_patient_modify_change_password);
        ib_patient_modify_password.setOnClickListener(new addListeners());
        ib_patient_modify_advice = findViewById(R.id.ib_patient_modify_advice);
        ib_patient_modify_advice.setOnClickListener(new addListeners());

    }

    /**
     * 滚轮文字的大小
     * 滚轮文字的颜色
     * 省份滚轮是否循环显示
     * 城市滚轮是否循环显示
     * 地区（县）滚轮是否循环显示
     * 滚轮显示的item个数
     * 滚轮item间距
     */
    public void initCityPicker(){
        cityPicker = new CityPicker.Builder(PatientMyInfoModifyActivity.this)
        .textSize(20)
        .title("地址选择")
        .backgroundPop(0xa0000000)
        .titleBackgroundColor("#0CB6CA")
        .titleTextColor("#000000")
        .backgroundPop(0xa0000000)
        .confirTextColor("#000000")
        .cancelTextColor("#000000")
        .province("xx省")
        .city("xx市")
        .district("xx区")
        .textColor(Color.parseColor("#000000"))
        .provinceCyclic(true)
        .cityCyclic(false)
        .districtCyclic(false)
        .visibleItemsCount(7)
        .itemPadding(10)
        .onlyShowProvinceAndCity(false)
        .build();

        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                String province = citySelected[0];
                String city = citySelected[1];
                String district = citySelected[2];
                ed_patient_modify_location.setText(province+city+district);
            }

            @Override
            public void onCancel() {

            }
        });

    }

    /**
     * 返回按钮
     * 确认 返回个人中心
     * 取消 停留当前界面
     */
    public void Sure () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改未保存，确定退出？");
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
            finish();
        })
                .setNegativeButton("取消", (dialog, which) -> {
                    //取消对话框，返回界面
                    dialog.cancel();
                }).create().show();
        //只有点击按钮才行，点击空白无用
        builder.setCancelable(false);
    }

    public void showToast (final String msg) {
        runOnUiThread(() -> Toast.makeText(PatientMyInfoModifyActivity.this, msg, Toast.LENGTH_SHORT).show());
    }

    /**
     * 监听事件
     * 完成 连接服务器，更新界面信息
     * 返回 跳转个人中心
     * 修改密码 跳转修改密码界面
     * 医嘱  跳转医嘱病历界面
     */
    private class addListeners implements View.OnClickListener {
        @Override
        public void onClick (View v) {
            switch (v.getId()) {
                case R.id.bt_patient_modify_complete:
                    et1 = ed_patient_modify_myname.getText().toString().trim();
                    et2 = ed_patient_modify_myage.getText().toString().trim();
                    et3 = ed_patient_modify_mysex.getText().toString().trim();
                    et4 = ed_patient_modify_location.getText().toString().trim();
                    @SuppressLint("ShowToast") Thread thread = new Thread(() -> {
                        String send = "";
                        Socket socket;
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("pID", myApplication.getAccount());
                            jsonObject.put("pName", et1);
                            jsonObject.put("pAge", et2);
                            jsonObject.put("pSex", et3);
                            jsonObject.put("pAddress", et4);
                            jsonObject.put("request_type", "7");
                            jsonObject.put("user_type", "p");
                            send = jsonObject.toString();
                            socket = SocketUtil.getSendSocket();
                            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                            out.writeUTF(send);
                            out.close();

                            Thread.sleep(1000);
                            socket = SocketUtil.setPort(2019);
                            DataInputStream dataInputStream = new DataInputStream(socket
                                    .getInputStream());
                            String message = dataInputStream.readUTF();

                            JSONObject jsonObject1 = new JSONObject(message);
                            result=jsonObject1.getString("update_result");
                            socket.close();
                            Thread.interrupted();
                        } catch (IOException | JSONException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                    thread.start();
                    switch (result) {
                        case "成功":
                            myUser.setName(et1);
                            myUser.setSex(et3);
                            myUser.setAge(Integer.parseInt(et2));
                            myUser.setAddress(et4);
                            Toast.makeText(PatientMyInfoModifyActivity.this, "个人信息修改成功", Toast
                                    .LENGTH_LONG).show();

                            finish();
                            break;
                        case "失败":
                            Toast.makeText(PatientMyInfoModifyActivity.this,"网络不稳定，请稍后再试~",Toast.LENGTH_LONG);
                            break;

                    }
                    break;
                case R.id.ib_patient_modify_return:
                    Sure();
                    break;
                case R.id.ib_patient_modify_change_password:
                    Intent intent = new Intent(PatientMyInfoModifyActivity.this,
                            PatientPasswordActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ib_patient_modify_advice:
                    startActivity(new Intent(PatientMyInfoModifyActivity.this,
                            PatientDoctorAdviceActivity.class));
                    break;
                case R.id.ed_patient_modify_location:
                    initCityPicker();
                    cityPicker.show();
                    break;
                default:
                    break;
            }
        }
    }
}
