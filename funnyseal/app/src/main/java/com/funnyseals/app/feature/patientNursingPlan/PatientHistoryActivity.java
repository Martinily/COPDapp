package com.funnyseals.app.feature.patientNursingPlan;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.funnyseals.app.feature.bottomtab.PatientBottomActivity;
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

public class PatientHistoryActivity extends AppCompatActivity {

    //数据传入
    private String mPatientID;
    private List<String> mSicker_historydates=new ArrayList<>();
    private List<String> mSicker_judgeuses=new ArrayList<>();
    private List<String> mSicker_planIDs=new ArrayList<>();

    //将数据封装成数据源
    List<Map<String,Object>> sickerhistory_list=new ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_history);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }

        //获取患者ID;
        Bundle bundle = this.getIntent().getExtras();
        mPatientID=bundle.getString("PatientID");

        Thread thread=new Thread(() -> {
            Socket socket;
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("ID",mPatientID);
                jsonObject.put("request_type", "4");
                jsonObject.put("query_state", "all");
                jsonObject.put("user_type","p");
                socket=SocketUtil.getSendSocket();
                DataOutputStream out=new DataOutputStream(socket.getOutputStream());
                out.writeUTF(jsonObject.toString());
                out.close();

                Thread.sleep(1000);

                socket=SocketUtil.getGetSocket();
                DataInputStream dataInputStream=new DataInputStream(socket.getInputStream());
                String message=dataInputStream.readUTF();
                socket.close();

                System.err.println(message);
                if(message.equals("empty")){
                    return;
                }

                JSONArray jsonArray=new JSONArray(message);
                int i;
                for( i=0;i<jsonArray.length();i++){
                    mSicker_historydates.add(jsonArray.getJSONObject(i).getString("planTime"));
                    mSicker_judgeuses.add(jsonArray.getJSONObject(i).getString("planAcceptS"));
                    mSicker_planIDs.add(jsonArray.getJSONObject(i).getString("planID"));
                    System.err.println(mSicker_historydates);
                }
                socket.close();
            } catch (JSONException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
            Thread.interrupted();
        });
        thread.start();
        while(thread.isAlive()){

        }

        int size = mSicker_historydates.size();
        int size2 = mSicker_judgeuses.size();
        int size3 = mSicker_planIDs.size();
        String[] sicker_historydate=(String[]) mSicker_historydates.toArray(new String[size]);//日期
        String[] sicker_judgeuse=(String[]) mSicker_judgeuses.toArray(new String[size2]);//是否接收
        String[] sicker_planID=(String[]) mSicker_planIDs.toArray(new String[size3]);


        //将数据封装成数据源
        for(int i=0;i<sicker_historydate.length;i++){
            Map<String,Object> map=new HashMap<String, Object>();
            map.put("sickerhistorydate",sicker_historydate[i]);
            if(sicker_judgeuse[i].equals("1"))
                map.put("sickerhistoryuse",R.drawable.used);
            else
                map.put("sickerhistoryuse",R.drawable.unused);
            map.put("sickerplanid", sicker_planID[i]);
            sickerhistory_list.add(map);
        }
        ListView listview=(ListView)findViewById(R.id.listViewsikerhistorydate);
        listview.setAdapter(new MyAdapter());

        //返回按钮事件监听
        Button quitsickerhistorydate=(Button) findViewById(R.id.quitsickerhistorydate);
        quitsickerhistorydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 =new Intent(PatientHistoryActivity.this, PatientBottomActivity.class);
                startActivity(intent2);
                finish();
            }
        });
    }


    //列表适配器
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return sickerhistory_list.size();
        }

        @Override
        public Object getItem(int position) {
            return sickerhistory_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View view;
            final ViewHolder mHolder;
            if(convertView==null){
                view= LayoutInflater.from(PatientHistoryActivity.this).inflate(R.layout.historydate_item,null);
                mHolder=new ViewHolder();
                mHolder.cardhistory_date=(TextView)view.findViewById(R.id.historytime);
                mHolder.cardhistory_judgeuse=(ImageView) view.findViewById(R.id.judgeuse);
                view.setTag(mHolder);  //将ViewHolder存储在View中
            }else {
                view=convertView;
                mHolder=(ViewHolder)view.getTag();  //重新获得ViewHolder
            }
            mHolder.cardhistory_date.setText(sickerhistory_list.get(position).get("sickerhistorydate").toString());
            mHolder.cardhistory_judgeuse.setImageResource((int)sickerhistory_list.get(position).get("sickerhistoryuse"));

            Button morehistory=(Button)view.findViewById(R.id.moredetailhistory);
            morehistory.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    //
                    Intent intent= new Intent(PatientHistoryActivity.this,PatientDetailHistoryActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putCharSequence("planid",sickerhistory_list.get(position).get("sickerplanid").toString());  //患者护理计划编号
                    bundle.putCharSequence("patientid",mPatientID);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Toast.makeText(PatientHistoryActivity.this, sickerhistory_list.get(position).get("sickerplanid").toString(),
                            Toast.LENGTH_SHORT).show();

                }
            });
            return view;
        }
    }
    //容器
    class ViewHolder{
        ImageView cardhistory_judgeuse;
        TextView cardhistory_date;
    }
}