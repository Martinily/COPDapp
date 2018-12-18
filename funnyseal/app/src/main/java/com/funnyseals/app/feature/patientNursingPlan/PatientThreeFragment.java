package com.funnyseals.app.feature.patientNursingPlan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.funnyseals.app.R;
import com.funnyseals.app.feature.MyApplication;
import com.funnyseals.app.model.SportsPlan;
import com.funnyseals.app.model.UserDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 患者端three fragment about sports
 */
public class PatientThreeFragment extends Fragment {
    private        MyApplication             mApplication;
    private        List<SportsPlan>          mSportsPlans;
    //将数据封装成数据源
    private        List<Map<String, Object>> mSports_list = new ArrayList<>();
    private List<String>  mSports_Titles=new ArrayList<>();//名称
    private List<String>  mSports_Contents=new ArrayList<>();  //时长
    private List<String>  mSports_attentions=new ArrayList<>();//注意事项
    private String[] sports_time={"10:00", "2:00", "3:00", "4:00", "3:00", "2:00", "1:00", "2:00", "3:00"}; //最近的时间

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_three, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSports_Titles=((PatientNursingPlanFragment)(PatientThreeFragment.this.getParentFragment())).getmSports_Titles();
        mSports_Contents=((PatientNursingPlanFragment)(PatientThreeFragment.this.getParentFragment())).getmSports_Contents();
        mSports_attentions=((PatientNursingPlanFragment)(PatientThreeFragment.this.getParentFragment())).getmSports_attentions();

        int size = mSports_Titles.size();
        int size2 = mSports_Contents.size();
        int size3 = mSports_attentions.size();

        String[] sports_Title = (String[]) mSports_Titles.toArray(new String[size]);
        String[] sports_Content = (String[]) mSports_Contents.toArray(new String[size2]);
        String[] sports_attention = (String[]) mSports_attentions.toArray(new String[size3]);


        for(int i=0;i<sports_Title.length;i++){
            Map<String,Object> map=new HashMap<String, Object>();
            map.put("sportstitle",sports_Title[i]);
            map.put("sportsimg",R.drawable.sports);
            map.put("sportscontent",sports_Content[i]);
            map.put("sportsattention",sports_attention[i]);
            map.put("sportstime",sports_time[i]);
            mSports_list.add(map);
        }
        ListView listview = getActivity().findViewById(R.id.listViewsports);
        listview.setAdapter(new MyAdapter());
    }

    //当前card的adapter
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mSports_list.size();
        }

        @Override
        public Object getItem(int position) {
            return mSports_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            PatientThreeFragment.ViewHolder mHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.sports_list_item, null);
                mHolder = new ViewHolder();
                mHolder.mCardsports_title = view.findViewById(R.id.sports);
                mHolder.mCardsports_image = view.findViewById(R.id.sportsImg);
                mHolder.mCardsports_content = view.findViewById(R.id.sportsnum);
                mHolder.mCardsports_attention = view.findViewById(R.id.sportsattention);
                mHolder.mCardsports_time = view.findViewById(R.id.sportstime);
                view.setTag(mHolder);
            } else {
                view = convertView;
                mHolder = (PatientThreeFragment.ViewHolder) view.getTag();
            }
            mHolder.mCardsports_title.setText(mSports_list.get(position).get("sportstitle").toString());
            mHolder.mCardsports_image.setImageResource((int) mSports_list.get(position).get("sportsimg"));
            mHolder.mCardsports_content.setText(mSports_list.get(position).get("sportscontent").toString());
            mHolder.mCardsports_attention.setText(mSports_list.get(position).get("sportsattention").toString());
            mHolder.mCardsports_time.setText(mSports_list.get(position).get("sportstime").toString());

            Button moretime = view.findViewById(R.id.moresportstime);
            moretime.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), SportsRetimeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putCharSequence("sportstitle", mSports_list.get(position).get("sportstitle").toString());
                intent.putExtras(bundle);
                startActivity(intent);
            });
            return view;
        }
    }

    //变量声明
    private class ViewHolder {
        private TextView  mCardsports_title;
        private ImageView mCardsports_image;
        private TextView  mCardsports_content;
        private TextView  mCardsports_attention;
        private TextView  mCardsports_time;
    }
}