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
import android.widget.Toast;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.util.BtnClickLimitUtil;
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
医生端详细历史护理计划
 */
public class DoctorDetailHistoryActivity extends AppCompatActivity {

    List<Map<String, Object>> medicine_list = new ArrayList<Map<String, Object>>();
    //接受护理计划编号和医生编号
    private String       mPlanId;
    private String       mDoctorId;
    private Button       mPickPatient;//发送给患者的按钮
    private String       mPatientId;//患者id
    private String       mSuccessSend;
    private List<String> mHistorymedicine_Titles       = new ArrayList<>();
    private List<String> mHistorymedicine_nums         = new ArrayList<>();
    private List<String> mHistorymedicine_attentions   = new ArrayList<>();
    private List<String> mHistorymedicine_times        = new ArrayList<>();
    private List<String> mHistoryinstrument_Titles     = new ArrayList<>();
    private List<String> mHistoryinstrument_nums       = new ArrayList<>();
    private List<String> mHistoryinstrument_attentions = new ArrayList<>();
    private List<String> mHistorysports_Titles         = new ArrayList<>();
    private List<String> mHistorysports_nums           = new ArrayList<>();
    private List<String> mHistorysports_attentions     = new ArrayList<>();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detailhistory);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //获取医生ID和护理计划编号;
        Bundle bundle = this.getIntent().getExtras();
        mDoctorId = bundle.getString("doctorid");
        mPlanId = bundle.getString("planid");

        //发送护理计划，跳转到患者列表
        mPickPatient = findViewById(R.id.sendhistory);
        mPickPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(BtnClickLimitUtil.isFastClick()){
                    Intent intent = new Intent(DoctorDetailHistoryActivity.this, PickPatientActivity
                            .class);
                    //传输医生编号
                    Bundle bundle = new Bundle();
                    bundle.putString("DoctorID", mDoctorId);//医生id
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1000);
                }
            }
        });

        //接收详细内容
        Thread thread = new Thread(() -> {
            Socket socket;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("planID", mPlanId);
                jsonObject.put("request_type", "5");
                socket = SocketUtil.getSendSocket();
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(jsonObject.toString());
                out.close();

                Thread.sleep(1000);

                socket = SocketUtil.getGetSocket();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String message = dataInputStream.readUTF();
                socket.close();

                if (message.equals("empty")) {
                    return;
                }

                JSONArray jsonArray = new JSONArray(message);
                int i;

                for (i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.getJSONObject(i).getString("item_type").equals("sports")) {
                        mHistorysports_Titles.add(jsonArray.getJSONObject(i).getString("sType"));
                        mHistorysports_nums.add(jsonArray.getJSONObject(i).getString("sTime"));
                        mHistorysports_attentions.add(jsonArray.getJSONObject(i).getString
                                ("sAttention"));
                    } else if (jsonArray.getJSONObject(i).getString("item_type").equals("med")) {
                        mHistorymedicine_Titles.add(jsonArray.getJSONObject(i).getString("mName"));
                        mHistorymedicine_nums.add(jsonArray.getJSONObject(i).getString("mDose"));
                        mHistorymedicine_attentions.add(jsonArray.getJSONObject(i).getString
                                ("mAttention"));
                        mHistorymedicine_times.add(jsonArray.getJSONObject(i).getString("mTime"));
                    } else if (jsonArray.getJSONObject(i).getString("item_type").equals("app")) {
                        mHistoryinstrument_Titles.add(jsonArray.getJSONObject(i).getString
                                ("appName"));
                        mHistoryinstrument_nums.add(jsonArray.getJSONObject(i).getString
                                ("appTime"));
                        mHistoryinstrument_attentions.add(jsonArray.getJSONObject(i).getString
                                ("appAttention"));
                    }
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

        //array到数组的转换
        int size = mHistorymedicine_Titles.size();
        int size2 = mHistoryinstrument_Titles.size();
        int size3 = mHistorysports_Titles.size();

        String[] historymedicine_Title = mHistorymedicine_Titles.toArray(new
                String[size]);//药物名称
        String[] historymedicine_num = mHistorymedicine_nums.toArray(new String[size])
                ;//药物数量
        String[] historymedicine_attention = mHistorymedicine_attentions.toArray(new
                String[size]);//药物注意事项
        String[] historymedicine_time = mHistorymedicine_times.toArray(new
                String[size]);//药物时间

        String[] historyinstrument_Title = mHistoryinstrument_Titles.toArray(new
                String[size2]);//器械名称
        String[] historyinstrument_num = mHistoryinstrument_nums.toArray(new
                String[size2]);//器械时长
        String[] historyinstrument_attention = mHistoryinstrument_attentions.toArray
                (new String[size2]);//器械注意事项

        String[] historysports_Title = mHistorysports_Titles.toArray(new
                String[size3]);//运动名称
        String[] historysports_num = mHistorysports_nums.toArray(new String[size3]);
        //运动时长
        String[] historysports_attention = mHistorysports_attentions.toArray(new
                String[size3]);//运动注意事项

        //分类（药物，器械，运动）存放
        String time;
        for (int i = 0; i < historymedicine_Title.length; i++) {
            time = "";
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("historytitle", historymedicine_Title[i]);
            map.put("historyimg", R.drawable.vector_drawable_pillow);
            map.put("historynum", "剂量：");
            map.put("historynumdetail", historymedicine_num[i]);
            map.put("historyattention", historymedicine_attention[i]);
            if (historymedicine_time[i].equals("-")) {
                time = "-";
            } else {
                if (historymedicine_time[i].charAt(0) == '1')
                    time += "早饭前 ";
                if (historymedicine_time[i].charAt(1) == '1')
                    time += "早饭后 ";
                if (historymedicine_time[i].charAt(2) == '1')
                    time += "午饭前 ";
                if (historymedicine_time[i].charAt(3) == '1')
                    time += "午饭后 ";
                if (historymedicine_time[i].charAt(4) == '1')
                    time += "晚饭前 ";
                if (historymedicine_time[i].charAt(5) == '1')
                    time += "晚饭后 ";
            }

            map.put("historytime", time);
            medicine_list.add(map);
        }
        for (int i = 0; i < historyinstrument_Title.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("historytitle", historyinstrument_Title[i]);
            map.put("historyimg", R.drawable.vector_drawable_instrument);
            map.put("historynum", "建议时长：");
            map.put("historynumdetail", historyinstrument_num[i]);
            map.put("historyattention", historyinstrument_attention[i]);
            map.put("historytime", "-");
            medicine_list.add(map);
        }
        for (int i = 0; i < historysports_Title.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("historytitle", historysports_Title[i]);
            map.put("historyimg", R.drawable.vector_drawable_sports);
            map.put("historynum", "建议时长：");
            map.put("historynumdetail", historysports_num[i]);
            map.put("historyattention", historysports_attention[i]);
            map.put("historytime", "-");
            medicine_list.add(map);
        }
        ListView listview = findViewById(R.id.listViewdoctorhistory);
        listview.setAdapter(new MyAdapter());

        //退出按钮事件监听
        Button quitdoctorhistory = findViewById(R.id.quitdochistory);
        quitdoctorhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finish();
            }
        });
    }

    //接收患者列表返回的患者Id
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == 1001) {
            mPatientId = data.getStringExtra("patientid");

            Thread thread =new Thread(() -> {
                Socket socket;
                JSONObject jsonObject = new JSONObject();
                MyApplication application = (MyApplication) DoctorDetailHistoryActivity.this
                        .getApplication();
                try {
                    jsonObject.put("request_type", "14");
                    jsonObject.put("update_type", "reUse");
                    jsonObject.put("planID", mPlanId);
                    //jsonObject.put("docID",application.getAccount());
                    jsonObject.put("docID", mDoctorId);// mDoctorID
                    jsonObject.put("pID", mPatientId);  //mPatientId
                    socket = SocketUtil.getSendSocket();
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    out.writeUTF(jsonObject.toString());
                    out.close();

                    Thread.sleep(1000);

                    socket = SocketUtil.getGetSocket();
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    String message = dataInputStream.readUTF();
                    JSONObject jsonObject3 = new JSONObject(message);
                    mSuccessSend=jsonObject3.getString("item_result");

                    socket.close();
                } catch (JSONException | IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                Thread.interrupted();
            });
            thread.start();
            while (thread.isAlive()) {

            }

            if(mSuccessSend.equals("true")) {
                Toast.makeText(DoctorDetailHistoryActivity.this, "发送成功！", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(DoctorDetailHistoryActivity.this, "发送失败，换个姿势试试~", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //列表的适配器
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount () {
            return medicine_list.size();
        }

        @Override
        public Object getItem (int position) {
            return medicine_list.get(position);
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
                view = LayoutInflater.from(DoctorDetailHistoryActivity.this).inflate(R.layout
                        .historydetail_item, null);
                mHolder = new ViewHolder();
                mHolder.cardhistory_title = view.findViewById(R.id.hismedicine);
                mHolder.cardhistory_image = view.findViewById(R.id.hismedicineImg);
                mHolder.cardhistory_num = view.findViewById(R.id.histype);
                mHolder.cardhistory_numdetail = view.findViewById(R.id.hisnum);
                mHolder.cardhistory_attention = view.findViewById(R.id.hisattention);
                mHolder.cardhistory_time = view.findViewById(R.id.histime);
                view.setTag(mHolder);  //将ViewHolder存储在View中
            } else {
                view = convertView;
                mHolder = (ViewHolder) view.getTag();  //重新获得ViewHolder
            }
            mHolder.cardhistory_title.setText(medicine_list.get(position).get("historytitle")
                    .toString());
            mHolder.cardhistory_image.setImageResource((int) medicine_list.get(position).get
                    ("historyimg"));
            mHolder.cardhistory_num.setText(medicine_list.get(position).get("historynum")
                    .toString());
            mHolder.cardhistory_numdetail.setText(medicine_list.get(position).get
                    ("historynumdetail").toString());
            mHolder.cardhistory_attention.setText(medicine_list.get(position).get
                    ("historyattention").toString());
            mHolder.cardhistory_time.setText(medicine_list.get(position).get("historytime")
                    .toString());

            //
            final String medicinenametime = medicine_list.get(position).get("historytitle")
                    .toString();
            //
            return view;
        }
    }

    //容器
    class ViewHolder {
        TextView  cardhistory_title;
        ImageView cardhistory_image;
        TextView  cardhistory_num;
        TextView  cardhistory_numdetail;
        TextView  cardhistory_attention;
        TextView  cardhistory_time;
    }
}