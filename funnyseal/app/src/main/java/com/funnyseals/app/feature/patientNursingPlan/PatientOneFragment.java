package com.funnyseals.app.feature.patientNursingPlan;

import android.content.Intent;
import android.os.Bundle;
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

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 患者端护理计划 onfragment about medicine
 */
public class PatientOneFragment extends Fragment {
    private static Connection                CONN;
    private        MyApplication             mApplication;
    private        List<MedicinePlan>        mMedicinePlans;
    //将数据封装成数据源
    private        List<Map<String, Object>> mMedicine_list       = new ArrayList<Map<String,
            Object>>();
    private        List<String>              mMedicine_Titles     = new ArrayList<>();
    private        List<String>              mMedicine_Contents   = new ArrayList<>();
    private        List<String>              mMedicine_attentions = new ArrayList<>();
    private        List<String>              mMedicine_needtimes  = new ArrayList<>();

    @Override
    public void onResume () {
        super.onResume();
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_one, null);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMedicine_Titles = ((PatientNursingPlanFragment) (PatientOneFragment.this
                .getParentFragment())).getmMedicine_Titles();
        mMedicine_Contents = ((PatientNursingPlanFragment) (PatientOneFragment.this
                .getParentFragment())).getmMedicine_Contents();
        mMedicine_attentions = ((PatientNursingPlanFragment) (PatientOneFragment.this
                .getParentFragment())).getmMedicine_attentions();
        mMedicine_needtimes = ((PatientNursingPlanFragment) (PatientOneFragment.this
                .getParentFragment())).getmMedicine_needtimes();
        int size = mMedicine_Titles.size();
        int size2 = mMedicine_Contents.size();
        int size3 = mMedicine_attentions.size();
        int size4 = mMedicine_needtimes.size();

        String[] medicine_Title = (String[]) mMedicine_Titles.toArray(new String[size]);
        String[] medicine_Content = (String[]) mMedicine_Contents.toArray(new String[size2]);
        String[] medicine_attention = (String[]) mMedicine_attentions.toArray(new String[size3]);
        String[] medicine_needtime = (String[]) mMedicine_needtimes.toArray(new String[size4]);

        for (int i = 0; i < medicine_Title.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("medicinetitle", medicine_Title[i]);
            map.put("medicineimg", R.drawable.vector_drawable_pillow);
            map.put("medicinecontent", medicine_Content[i]);
            map.put("medicineattention", medicine_attention[i]);
            String needtime = "";
            if (medicine_needtime[i].equals("-")) {
                needtime = "-";
            } else {
                if (medicine_needtime[i].charAt(0) == '1')
                    needtime += "早饭前 ";
                if (medicine_needtime[i].charAt(1) == '1')
                    needtime += "早饭后 ";
                if (medicine_needtime[i].charAt(2) == '1')
                    needtime += "午饭前 ";
                if (medicine_needtime[i].charAt(3) == '1')
                    needtime += "午饭后 ";
                if (medicine_needtime[i].charAt(4) == '1')
                    needtime += "晚饭前 ";
                if (medicine_needtime[i].charAt(5) == '1')
                    needtime += "晚饭后 ";
            }
            map.put("realMedicineTime", needtime);
            mMedicine_list.add(map);

        }
        ListView listview = getActivity().findViewById(R.id.listViewmedicine);
        listview.setAdapter(new MyAdapter());
    }

    //当前card adapter
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount () {
            return mMedicine_list.size();
        }

        @Override
        public Object getItem (int position) {
            return mMedicine_list.get(position);
        }

        @Override
        public long getItemId (int position) {
            return position;
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder mHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.medicine_list_item,
                        null);
                mHolder = new ViewHolder();
                mHolder.mCardmedicine_title = view.findViewById(R.id.medicine);
                mHolder.mCardmedicine_image = view.findViewById(R.id.medicineImg);
                mHolder.mCardmedicine_content = view.findViewById(R.id.medicinenum);
                mHolder.mCardmedicine_attention = view.findViewById(R.id.medicineattention);
                mHolder.mCardmedicine_time = view.findViewById(R.id.medicinetime);
                view.setTag(mHolder);  //将ViewHolder存储在View中
            } else {
                view = convertView;
                mHolder = (ViewHolder) view.getTag();  //重新获得ViewHolder
            }
            mHolder.mCardmedicine_title.setText(mMedicine_list.get(position).get("medicinetitle")
                    .toString());
            mHolder.mCardmedicine_image.setImageResource((int) mMedicine_list.get(position).get
                    ("medicineimg"));
            mHolder.mCardmedicine_content.setText(mMedicine_list.get(position).get
                    ("medicinecontent").toString());
            mHolder.mCardmedicine_attention.setText(mMedicine_list.get(position).get
                    ("medicineattention").toString());
            mHolder.mCardmedicine_time.setText(mMedicine_list.get(position).get
                    ("realMedicineTime").toString());

            Button moretime = view.findViewById(R.id.moremedicinetime);
            moretime.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), MedicineRetimeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putCharSequence("medicinetitle", mMedicine_list.get(position).get
                        ("medicinetitle").toString());
                intent.putExtras(bundle);
                startActivity(intent);
            });
            return view;
        }
    }

    //变量声明
    private class ViewHolder {
        private TextView  mCardmedicine_title;
        private ImageView mCardmedicine_image;
        private TextView  mCardmedicine_content;
        private TextView  mCardmedicine_attention;
        private TextView  mCardmedicine_time;
    }
}