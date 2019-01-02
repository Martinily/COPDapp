package com.funnyseals.app.feature.doctorNursingPlan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
医生端历史护理计划
 */
public class DoctorHistoryActivity extends AppCompatActivity {

    //将数据封装成数据源
    List<Map<String, Object>> doctorhistory_list = new ArrayList<Map<String, Object>>();
    //接受医生账号
    private String       mDoctorID;
    //数据获取
    private List<String> mDoctor_historydates = new ArrayList<>();
    private List<String> mDoctor_historyUses  = new ArrayList<>();
    private List<String> mDoctor_historyIds   = new ArrayList<>();

    protected void onRestart () {
        super.onRestart();
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_history);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Bundle bundle = this.getIntent().getExtras();
        mDoctorID = bundle.getString("DoctorID");

        Thread thread = new Thread(() -> {
            Socket socket;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ID", mDoctorID);
                jsonObject.put("request_type", "4");
                jsonObject.put("user_type", "d");
                socket = SocketUtil.getSendSocket();
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(jsonObject.toString());
                out.close();

                Thread.sleep(1000);

                socket = SocketUtil.getGetSocket();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String message = dataInputStream.readUTF();
                if (message.equals("empty")) {
                    return;
                }

                JSONArray jsonArray = new JSONArray(message);
                int i;
                for (i = 0; i < jsonArray.length(); i++) {
                    mDoctor_historydates.add(jsonArray.getJSONObject(i).getString("planTime"));
                    mDoctor_historyUses.add(jsonArray.getJSONObject(i).getString("planAcceptS"));
                    mDoctor_historyIds.add(jsonArray.getJSONObject(i).getString("planID"));
                }
                dataInputStream.close();
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
            } catch (JSONException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        while (thread.isAlive()) {

        }

        //array到数组的转换
        int size = mDoctor_historydates.size();
        int size2 = mDoctor_historyUses.size();
        int size3 = mDoctor_historyIds.size();
        String[] doctor_historydate = mDoctor_historydates.toArray(new String[size]);
        String[] mDoctor_historyUse = mDoctor_historyUses.toArray(new String[size2]);
        String[] mDoctor_historyId = mDoctor_historyIds.toArray(new String[size3]);

        //将数据封装成数据源
        for (int i = 0; i < doctor_historydate.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("doctorhistorydate", doctor_historydate[i]);
            if (mDoctor_historyUse[i].equals("1")) {
                map.put("doctorhistoryuse", R.drawable.vector_drawable_used);
            } else
                map.put("doctorhistoryuse", R.drawable.vector_drawable_unused);
            map.put("doctorhistoryid", mDoctor_historyId[i]);
            doctorhistory_list.add(map);
        }
        ListView listview = findViewById(R.id.listViewdoctorhistorydate);
        listview.setAdapter(new MyAdapter());

        //返回按钮事件监听
        Button quitdoctorhistorydate = findViewById(R.id.quitdoctorhistorydate);
        quitdoctorhistorydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finish();
            }
        });
    }

    //列表适配器
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount () {
            return doctorhistory_list.size();
        }

        @Override
        public Object getItem (int position) {
            return doctorhistory_list.get(position);
        }

        @Override
        public long getItemId (int position) {
            return position;
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            final View view;
            final ViewHolder mHolder;
            if (convertView == null) {
                view = LayoutInflater.from(DoctorHistoryActivity.this).inflate(R.layout
                        .historydate_item, null);
                mHolder = new ViewHolder();
                mHolder.cardhistory_date = view.findViewById(R.id.historytime);
                mHolder.cardhistory_judgeuse = view.findViewById(R.id.judgeuse);
                view.setTag(mHolder);  //将ViewHolder存储在View中
            } else {
                view = convertView;
                mHolder = (ViewHolder) view.getTag();  //重新获得ViewHolder
            }
            mHolder.cardhistory_date.setText(doctorhistory_list.get(position).get
                    ("doctorhistorydate").toString());
            mHolder.cardhistory_judgeuse.setImageResource((int) doctorhistory_list.get(position)
                    .get("doctorhistoryuse"));

            //详细的某一项历史计划的查看
            Button moredetailhistory = view.findViewById(R.id.moredetailhistory);
            moredetailhistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    //
                    Intent intent = new Intent(DoctorHistoryActivity.this,
                            DoctorDetailHistoryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putCharSequence("planid", doctorhistory_list.get(position).get
                            ("doctorhistoryid").toString());  //患者护理计划编号
                    bundle.putCharSequence("doctorid", mDoctorID);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            return view;
        }
    }

    //容器
    class ViewHolder {
        ImageView cardhistory_judgeuse;
        TextView  cardhistory_date;
    }
}