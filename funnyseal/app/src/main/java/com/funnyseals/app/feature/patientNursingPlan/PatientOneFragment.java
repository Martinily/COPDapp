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
import com.funnyseals.app.model.MedicinePlan;
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
 *
 */
public class PatientOneFragment extends Fragment {
    private static Connection conn;
    MyApplication application;
    private List<MedicinePlan> medicinePlans;
    //将数据封装成数据源
    List<Map<String, Object>> medicine_list = new ArrayList<Map<String, Object>>();
    private Thread  mThread;
    //用于执行数据库线程
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                medicinePlans = (List<MedicinePlan>) msg.obj;

                //将数据封装成数据源
                for (MedicinePlan p : medicinePlans) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("medicinetitle", p.getMedicineName());
                    map.put("medicineimg", R.drawable.pillow);
                    map.put("medicinecontent", p.getMedicineDose());
                    map.put("medicineattention", p.getMedicineNote());
                    map.put("medicinetime", "10:00");
                    map.put("realMedicineTime", p.getMedicineTime());
                    medicine_list.add(map);
                }

                ListView listview = getActivity().findViewById(R.id.listViewmedicine);
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
        return inflater.inflate(R.layout.fragment_patient_one, null);
    }

    @Override
    public void onResume() {
        super.onResume();
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
                    PreparedStatement medicineS = conn.prepareStatement("SELECT * FROM HLJHYW WHERE HLJH_BH=?");
                    medicineS.setString(1, rs.getString(1));
                    rs = medicineS.executeQuery();
                    medicinePlans = new ArrayList<>();
                    while (rs.next()) {
                        medicinePlans.add(new MedicinePlan(rs.getString("HLJHYW_YWMC"), rs.getString("HLJHYW_YWJL"), rs.getString("HLJHYW_ZYSX"), rs.getString("HLJHYW_FYSJ")));
                    }
                    Message message = Message.obtain();
                    message.what = 0;
                    message.obj = medicinePlans;
                    mHandler.sendMessage(message);
                    conn.close();
                    medicineS.close();
                    rs.close();
                    statement.close();
                }
            } else {
                System.err.println("警告: DbConnectionManager.getConnection() 获得数据库链接失败.");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return medicine_list.size();
        }

        @Override
        public Object getItem(int position) {
            return medicine_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder mHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.medicine_list_item, null);
                mHolder = new ViewHolder();
                mHolder.cardmedicine_title = view.findViewById(R.id.medicine);
                mHolder.cardmedicine_image = view.findViewById(R.id.medicineImg);
                mHolder.cardmedicine_content = view.findViewById(R.id.medicinenum);
                mHolder.cardmedicine_attention = view.findViewById(R.id.medicineattention);
                mHolder.cardmedicine_time = view.findViewById(R.id.medicinetime);
                view.setTag(mHolder);  //将ViewHolder存储在View中
            } else {
                view = convertView;
                mHolder = (ViewHolder) view.getTag();  //重新获得ViewHolder
            }
            mHolder.cardmedicine_title.setText(medicine_list.get(position).get("medicinetitle").toString());
            mHolder.cardmedicine_image.setImageResource((int) medicine_list.get(position).get("medicineimg"));
            mHolder.cardmedicine_content.setText(medicine_list.get(position).get("medicinecontent").toString());
            mHolder.cardmedicine_attention.setText(medicine_list.get(position).get("medicineattention").toString());
            mHolder.cardmedicine_time.setText(medicine_list.get(position).get("medicinetime").toString());
            final String medicinenametime = medicine_list.get(position).get("realMedicineTime").toString();
            System.err.println(medicine_list.get(position).get("realMedicineTime").toString());
            Button moretime = view.findViewById(R.id.moremedicinetime);

            moretime.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), MedicineRetimeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putCharSequence("medicinenametime", medicinenametime);
                intent.putExtras(bundle);
                startActivity(intent);
            });
            return view;
        }
    }

    class ViewHolder {
        TextView  cardmedicine_title;
        ImageView cardmedicine_image;
        TextView  cardmedicine_content;
        TextView  cardmedicine_attention;
        TextView  cardmedicine_time;
    }
}
