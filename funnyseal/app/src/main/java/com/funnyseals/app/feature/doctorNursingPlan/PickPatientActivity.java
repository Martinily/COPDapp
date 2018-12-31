package com.funnyseals.app.feature.doctorNursingPlan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.funnyseals.app.R;
import com.funnyseals.app.util.SocketUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/*
医生端(发送护理计划->)选择患者
 */
public class PickPatientActivity extends AppCompatActivity {

    private String                mDoctorId;
    private ListView              mPatientlistView;
    private MyListViewAdapter     mListViewAdapter;
    private List<PickPatientBean> mPatientBeanList = new ArrayList<PickPatientBean>();
    private List<String>          mPatients        = new ArrayList<>();
    private List<String>          mPatientsIds     = new ArrayList<>();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_patient);

        ActionBar actionBar = getSupportActionBar();   //隐藏自带actionBar
        if (actionBar != null) {
            actionBar.hide();
        }

        //获取医生ID;
        Bundle bundle = this.getIntent().getExtras();
        mDoctorId = bundle.getString("DoctorID");

        //获取该医生的患者
        Thread thread = new Thread(() -> {
            Socket socket;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("docID", mDoctorId);
                jsonObject.put("request_type", "12");
                socket = SocketUtil.getSendSocket();
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(jsonObject.toString());
                out.close();

                Thread.sleep(1000);

                socket = SocketUtil.getArraySendSocket3();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String message = dataInputStream.readUTF();
                socket.close();

                if (message.equals("empty")) {
                    return;
                }

                JSONArray jsonArray = new JSONArray(message);
                int i;

                for (i = 0; i < jsonArray.length(); i++) {
                    mPatients.add(jsonArray.getJSONObject(i).getString("pName"));
                    mPatientsIds.add(jsonArray.getJSONObject(i).getString("pID"));
                }
                socket.close();
            } catch (JSONException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
            Thread.interrupted();
        });
        thread.start();
        while (thread.isAlive()) {

        }

        int size = mPatients.size();
        int size2 = mPatientsIds.size();
        String[] mPatient = mPatients.toArray(new String[size]);
        String[] mPatientsId = mPatientsIds.toArray(new String[size2]);

        //加载mPatientlistView
        mPatientlistView = findViewById(R.id.pickpatientlistView);
        mListViewAdapter = new MyListViewAdapter(PickPatientActivity.this, mPatientBeanList);
        mPatientlistView.setAdapter(mListViewAdapter);

        mPatientlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> adapterView, View view, int position, long id) {
                PickPatientBean patientBean = mPatientBeanList.get(position);
                String patientname = patientBean.getName();
                String patientid = patientBean.getId();
                new AlertDialog.Builder(PickPatientActivity.this).setTitle("我的提示").setMessage
                        ("确认发送？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick (DialogInterface dialog, int which) {
                                Intent intent2 = new Intent();
                                Bundle bundle2 = new Bundle();
                                bundle2.putCharSequence("patientid", patientid);
                                intent2.putExtras(bundle2);
                                setResult(1001, intent2);
                                finish();
                            }
                        }).show();
            }
        });

        for (int i = 0; i < mPatient.length; i++) {
            PickPatientBean patientBean = new PickPatientBean(mPatient[i]);
            mPatientBeanList.add(patientBean);
            patientBean.setId(mPatientsId[i]);
            mListViewAdapter.notifyDataSetChanged();
        }

        //退出的点击事件
        final Button medicinesaveall = findViewById(R.id.quitpickpitient);
        medicinesaveall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                new AlertDialog.Builder(PickPatientActivity.this).setTitle("我的提示").setMessage
                        ("确定要退出发送吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick (DialogInterface dialog, int which) {
                                finish();
                            }
                        }).show();
            }
        });
    }

    //列表适配器
    public class MyListViewAdapter extends BaseAdapter {

        private Context mContext;

        /**
         * 数据
         */
        private String                name;
        private List<PickPatientBean> BeanList;

        /**
         * 构造函数
         */
        public MyListViewAdapter (Context context, List<PickPatientBean> BeanList) {
            this.mContext = context;
            this.BeanList = BeanList;
        }

        @Override
        public int getCount () {
            return BeanList.size();
        }

        @Override
        public Object getItem (int position) {
            return null;
        }

        @Override
        public long getItemId (int position) {
            return 0;
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView != null) {
                view = convertView;
            } else {
                view = View.inflate(mContext, R.layout.pickpatient_listitem, null);
            }

            PickPatientBean bean = BeanList.get(position);
            if (bean == null) {
                bean = new PickPatientBean("NoName");
            }

            //更新数据
            final TextView nameTextView = view.findViewById(R.id.patientName);
            nameTextView.setText(bean.getName());

            final int removePosition = position;

            return view;
        }

        public String getName () {
            return name;
        }
    }
}