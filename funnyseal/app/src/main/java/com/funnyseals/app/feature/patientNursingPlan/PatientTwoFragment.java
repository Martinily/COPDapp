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
import com.funnyseals.app.model.InstrumentPlan;
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

 */
public class PatientTwoFragment extends Fragment {
    private        View       view;
    private static Connection conn;
    MyApplication application;
    private List<InstrumentPlan> instrumentPlans;
    //将数据封装成数据源
    List<Map<String, Object>> instrument_list = new ArrayList<>();
    private Thread  mThread;
    //用于执行数据库线程
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                instrumentPlans = (List<InstrumentPlan>) msg.obj;

                //将数据封装成数据源
                for (InstrumentPlan p : instrumentPlans) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("instrumenttitle", p.getInstrumentName());
                    map.put("instrumentimg", R.drawable.instrument);
                    map.put("instrumentcontent", p.getInstrumentTracks());
                    map.put("instrumentattention", p.getInstrumentNote());
                    map.put("instrumenttime", "10:00");
                    instrument_list.add(map);
                }
                ListView listview = getActivity().findViewById(R.id.listViewinstrument);
                listview.setAdapter(new MyAdapter());
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (mThread == null) {
            mThread = new Thread(runnable);
            mThread.start();
        }
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(R.layout.fragment_patient_two, null);
        return view;
    }

    Runnable runnable = () -> {
        try {
            conn = UserDao.getConnection();
            application = (MyApplication) getActivity().getApplication();
            String id = application.getAccount();
            if (conn != null) {
                PreparedStatement statement = conn.prepareStatement("SELECT HLJH_BH FROM HLJH WHERE HZ_ZH=? and HLJH_SYZT=1");
                statement.setString(1, id);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    PreparedStatement instrumentS = conn.prepareStatement("SELECT * FROM HLJHQX WHERE HLJH_BH=?");  //！！！！
                    instrumentS.setString(1, rs.getString(1));
                    rs = instrumentS.executeQuery();
                    instrumentPlans = new ArrayList<>();
                    while (rs.next()) {
                        instrumentPlans.add(new InstrumentPlan(rs.getString("HLJHQX_QXMC"), rs.getString("HLJHQX_SYSC"), rs.getString("HLJHQX_ZYSX")));
                    }
                    Message message = Message.obtain();
                    message.what = 0;
                    message.obj = instrumentPlans;
                    mHandler.sendMessage(message);
                    conn.close();
                    instrumentS.close();
                    rs.close();
                    statement.close();
                }
            } else {
                System.err.println("警告: DbConnectionManager.getConnection() 获得数据库链接失败.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return instrument_list.size();
        }

        @Override
        public Object getItem(int position) {
            return instrument_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            PatientTwoFragment.ViewHolder mHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.instrument_list_item, null);
                mHolder = new ViewHolder();
                mHolder.cardinstrument_title = view.findViewById(R.id.instrument);
                mHolder.cardinstrument_image = view.findViewById(R.id.instrumentImg);
                mHolder.cardinstrument_content = view.findViewById(R.id.instrumentnum);
                mHolder.cardinstrument_attention = view.findViewById(R.id.instrumentattention);
                mHolder.cardinstrument_time = view.findViewById(R.id.instrumenttime);
                view.setTag(mHolder);  //将ViewHolder存储在View中
            } else {
                view = convertView;
                mHolder = (PatientTwoFragment.ViewHolder) view.getTag();  //重新获得ViewHolder
            }
            mHolder.cardinstrument_title.setText(instrument_list.get(position).get("instrumenttitle").toString());
            mHolder.cardinstrument_image.setImageResource((int) instrument_list.get(position).get("instrumentimg"));
            mHolder.cardinstrument_content.setText(instrument_list.get(position).get("instrumentcontent").toString());
            mHolder.cardinstrument_attention.setText(instrument_list.get(position).get("instrumentattention").toString());
            mHolder.cardinstrument_time.setText(instrument_list.get(position).get("instrumenttime").toString());

            Button moretime = view.findViewById(R.id.moreinstrumenttime);
            moretime.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), InstrumentRetimeActivity.class);
                startActivity(intent);
            });
            return view;
        }

    }

    class ViewHolder {
        TextView  cardinstrument_title;
        ImageView cardinstrument_image;
        TextView  cardinstrument_content;
        TextView  cardinstrument_attention;
        TextView  cardinstrument_time;
    }
}
