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
import com.funnyseals.app.model.InstrumentPlan;
import com.funnyseals.app.util.BtnClickLimitUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 患者端护理计划two fragment about instrument
 */
public class PatientTwoFragment extends Fragment {
    private View                 mView;
    private List<String>         mInstrument_Titles     = new ArrayList<>();
    private List<String>         mInstrument_Contents   = new ArrayList<>();
    private List<String>         mInstrument_attentions = new ArrayList<>();
    //将数据封装成数据源
    private List<Map<String, Object>> mInstrument_list = new ArrayList<>();

    @Override
    public void onResume () {
        super.onResume();
        //将数据封装成数据源
        mInstrument_list = new ArrayList<>();
        mInstrument_Titles     = new ArrayList<>();
        mInstrument_Contents   = new ArrayList<>();
        mInstrument_attentions = new ArrayList<>();
        SetMessage();
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {

        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
            return mView;
        }
        mView = inflater.inflate(R.layout.fragment_patient_two, null);
        return mView;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SetMessage();
    }

    //得到计划内容
    public void SetMessage(){
        mInstrument_Titles = ((PatientNursingPlanFragment) (PatientTwoFragment.this
                .getParentFragment())).getmInstrument_Titles();
        mInstrument_Contents = ((PatientNursingPlanFragment) (PatientTwoFragment.this
                .getParentFragment())).getmInstrument_Contents();
        mInstrument_attentions = ((PatientNursingPlanFragment) (PatientTwoFragment.this
                .getParentFragment())).getmInstrument_attentions();

        int size = mInstrument_Titles.size();
        int size2 = mInstrument_Contents.size();
        int size3 = mInstrument_attentions.size();

        String[] instrument_Title = mInstrument_Titles.toArray(new String[size]);
        String[] instrument_Content = mInstrument_Contents.toArray(new String[size2]);
        String[] instrument_attention = mInstrument_attentions.toArray(new
                String[size3]);

        //将数据封装成数据源
        //将数据封装成数据源
        for (int i = 0; i < instrument_Title.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("instrumenttitle", instrument_Title[i]);
            map.put("instrumentimg", R.drawable.vector_drawable_instrument);
            map.put("instrumentcontent", instrument_Content[i]);
            map.put("instrumentattention", instrument_attention[i]);
            map.put("instrumenttime", "-");

            mInstrument_list.add(map);
        }
        ListView listview = getActivity().findViewById(R.id.listViewinstrument);
        listview.setAdapter(new MyAdapter());

    }

    //当前card adapter
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount () {
            return mInstrument_list.size();
        }

        @Override
        public Object getItem (int position) {
            return mInstrument_list.get(position);
        }

        @Override
        public long getItemId (int position) {
            return position;
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            View view;
            PatientTwoFragment.ViewHolder mHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.instrument_list_item,
                        null);
                mHolder = new ViewHolder();
                mHolder.mCardinstrument_title = view.findViewById(R.id.instrument);
                mHolder.mCardinstrument_image = view.findViewById(R.id.instrumentImg);
                mHolder.mCardinstrument_content = view.findViewById(R.id.instrumentnum);
                mHolder.mCardinstrument_attention = view.findViewById(R.id.instrumentattention);
                mHolder.mCardinstrument_time = view.findViewById(R.id.instrumenttime);
                view.setTag(mHolder);  //将ViewHolder存储在View中
            } else {
                view = convertView;
                mHolder = (PatientTwoFragment.ViewHolder) view.getTag();  //重新获得ViewHolder
            }
            mHolder.mCardinstrument_title.setText(mInstrument_list.get(position).get
                    ("instrumenttitle").toString());
            mHolder.mCardinstrument_image.setImageResource((int) mInstrument_list.get(position)
                    .get("instrumentimg"));
            mHolder.mCardinstrument_content.setText(mInstrument_list.get(position).get
                    ("instrumentcontent").toString());
            mHolder.mCardinstrument_attention.setText(mInstrument_list.get(position).get
                    ("instrumentattention").toString());
            mHolder.mCardinstrument_time.setText(mInstrument_list.get(position).get
                    ("instrumenttime").toString());

            Button moretime = view.findViewById(R.id.moreinstrumenttime);
            moretime.setOnClickListener(v -> {
                if(BtnClickLimitUtil.isFastClick()) {
                    Intent intent = new Intent(getActivity(), InstrumentRetimeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putCharSequence("instrumenttitle", mInstrument_list.get(position).get
                            ("instrumenttitle").toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            return view;
        }
    }

    //变量声明
    private class ViewHolder {
        private TextView  mCardinstrument_title;
        private ImageView mCardinstrument_image;
        private TextView  mCardinstrument_content;
        private TextView  mCardinstrument_attention;
        private TextView  mCardinstrument_time;
    }
}