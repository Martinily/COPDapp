package com.funnyseals.app.feature.doctorNursingPlan;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.funnyseals.app.R;
import com.funnyseals.app.util.SocketUtil;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.funnyseals.app.R.id.edit_instrument;

/**
 * 护理计划two fragment, about instrument
 */
public class DoctorTwoFragment extends Fragment {
    private EditText          mEditText;
    private ListView          mListView;
    private MyListViewAdapter mListViewAdapter;
    private List<Bean>        mInstrumentBeanList   = new ArrayList<Bean>();
    private List<String>      mInstrumentNames      = new ArrayList<>();
    private List<String>      mInstrumentAttentions = new ArrayList<>();

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_two, null);
        LoadMenu();
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mEditText = getActivity().findViewById(edit_instrument);
        mEditText.setOnTouchListener((view, event) -> {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getX() >= (mEditText.getWidth() - mEditText
                        .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                    mEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources()
                            .getDrawable(R.drawable.ic_nadown), null);
                    ShowListPopulWindow();
                    return true;
                }
            }
            return false;
        });
        mEditText.setOnFocusChangeListener((view, b) -> {
            if (b) {
                ShowListPopulWindow();
            }
        });

        //加载listview
        mListView = getActivity().findViewById(R.id.listViewinstrument);
        mListViewAdapter = new MyListViewAdapter(getActivity(), mInstrumentBeanList);
        mListView.setAdapter(mListViewAdapter);

        //save button的点击事件
        Button saveButton = getActivity().findViewById(R.id.addinstrument);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                SaveinstrumentMessage();
            }
        });

        mListView.setOnItemClickListener((adapterView, view, position, id) -> {
            Bean instrumentBean = mInstrumentBeanList.get(position);
            String instrumentname = instrumentBean.getName();
            String instrumentcontent = instrumentBean.getContent();
            String instrumentattention = instrumentBean.getAttention();
            Intent intent = new Intent(getActivity(), InstrumentDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putCharSequence("position", position + "");
            bundle.putCharSequence("instrumentname", instrumentname);
            bundle.putCharSequence("instrumentcontent", instrumentcontent);
            bundle.putCharSequence("instrumentattention", instrumentattention);
            intent.putExtras(bundle);
            startActivityForResult(intent, 1000);
        });

    }

    //接收返回数据
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == 1001) {
            int nowposition = Integer.valueOf(data.getStringExtra("reposition")).intValue();
            Bean instrumentBean = mInstrumentBeanList.get(nowposition);
            instrumentBean.setattention(data.getStringExtra("reinstrumentattention"));
            instrumentBean.setcontent(data.getStringExtra("reinstrumentnum"));
        }
    }

    //下拉列表内容的获取
    public void LoadMenu () {
        new Thread(() -> {
            Socket socket;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("request_type", "13");
                jsonObject.put("base_type", "app");
                socket = SocketUtil.getSendSocket();
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(jsonObject.toString());
                out.close();

                Thread.sleep(4000);

                socket = SocketUtil.getArraySendSocket2();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String message = dataInputStream.readUTF();
                socket.close();
                System.err.println(message);
                if (message.equals("empty")) {
                    return;
                }

                JSONArray jsonArray = new JSONArray(message);
                int i;

                for (i = 0; i < jsonArray.length(); i++) {
                    mInstrumentNames.add(jsonArray.getJSONObject(i).getString("apparatusName"));
                    mInstrumentAttentions.add(jsonArray.getJSONObject(i).getString
                            ("apparatusRemarks"));
                }
                socket.close();
            } catch (JSONException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    //展示下拉列表
    private void ShowListPopulWindow () {
        int size = mInstrumentNames.size();
        String[] instrumentss = (String[]) mInstrumentNames.toArray(new String[size]);
        final ListPopupWindow listPopupWindow;
        listPopupWindow = new ListPopupWindow(getActivity());
        listPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout
                .simple_list_item_1, instrumentss));
        listPopupWindow.setAnchorView(mEditText);
        listPopupWindow.setModal(true);

        //设置项点击监听
        listPopupWindow.setOnItemClickListener((adapterView, view, i, l) -> {
            mEditText.setText(instrumentss[i]);
            listPopupWindow.dismiss();
        });
        listPopupWindow.show();
        listPopupWindow.setOnDismissListener(() -> mEditText
                .setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R
                        .drawable.vector_drawable_na), null));
    }

    //删除已添加药物
    public void ShowInfo (final int position) {
        new AlertDialog.Builder(getActivity()).setTitle("我的提示").setMessage("确定要删除吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialog, int which) {
                        mInstrumentBeanList.remove(position);
                        ((DoctorNursingPlanFragment) (DoctorTwoFragment.this.getParentFragment())
                        ).ChangemPlannum(-1);
                        mListViewAdapter.notifyDataSetChanged();
                        ((DoctorNursingPlanFragment) (DoctorTwoFragment.this.getParentFragment())
                        ).deletemAllInstrumentItem(position);
                    }
                }).show();
    }

    //添加器械名称加到列表
    private void SaveinstrumentMessage () {
        EditText nameEditText = getActivity().findViewById(edit_instrument);

        if (StringUtils.isEmpty(nameEditText.getText().toString())) {
            Toast.makeText(getActivity(), "器械名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        int size = mInstrumentNames.size();
        String[] instrumentss = (String[]) mInstrumentNames.toArray(new String[size]);
        int size2 = mInstrumentAttentions.size();
        String[] instrumentattention = (String[]) mInstrumentAttentions.toArray(new String[size2]);
        String attention = "";
        for (int j = 0; j < size; j++) {
            if (nameEditText.getText().toString().equals(instrumentss[j])) {
                attention = instrumentattention[j];
            }
        }

        //判断该器械是否存在
        for (Bean instrumentBean : mInstrumentBeanList) {
            if (StringUtils.equals(instrumentBean.getName(), nameEditText.getText().toString())) {
                Toast.makeText(getActivity(), nameEditText.getText().toString() + "已经存在", Toast
                        .LENGTH_SHORT).show();
                return;
            }
        }
        Bean instrumentBean = new Bean(nameEditText.getText().toString());
        instrumentBean.setcontent("");
        instrumentBean.setattention(attention);
        mInstrumentBeanList.add(instrumentBean);
        ((DoctorNursingPlanFragment) (DoctorTwoFragment.this.getParentFragment())).ChangemPlannum
                (1);
        ((DoctorNursingPlanFragment) (DoctorTwoFragment.this.getParentFragment()))
                .setmAllInstrumentItem(instrumentBean);
        mListViewAdapter.notifyDataSetChanged();
        //((MainActivity)getActivity()).setmtestinstrument("0");
    }

    //列表适配器
    public class MyListViewAdapter extends BaseAdapter {

        private Context mContext;

        /**
         * 数据
         */
        private String     name;
        private List<Bean> BeanList;

        /**
         * 构造函数
         */
        public MyListViewAdapter (Context context, List<Bean> BeanList) {
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
                view = View.inflate(mContext, R.layout.list_view, null);
            }

            Bean bean = BeanList.get(position);
            if (bean == null) {
                bean = new Bean("NoName");
            }

            //更新数据
            final TextView nameTextView = (TextView) view.findViewById(R.id.showName);
            nameTextView.setText(bean.getName());

            final int removePosition = position;

            //删除按钮点击事件
            final Button deleteButton = (Button) view.findViewById(R.id.showDeleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    deleteButtonAction(removePosition);
                    mListViewAdapter.notifyDataSetChanged();
                }
            });
            return view;
        }

        public void deleteButtonAction (int position) {
            ShowInfo(position);
            notifyDataSetChanged();
        }

        public String getName () {
            return name;
        }
    }
}