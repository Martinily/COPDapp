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
import com.funnyseals.app.feature.bottomtab.DoctorBottomActivity;
import com.funnyseals.app.feature.patientNursingPlan.PatientNursingPlanFragment;
import com.funnyseals.app.feature.patientNursingPlan.PatientOneFragment;
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
import java.util.Objects;

import static com.funnyseals.app.R.id.edit_medicine;

/*
护理计划Onefragment,about medicine
 */
public class DoctorOneFragment extends Fragment {
    private EditText          mEditText;
    private ListView          mListView;
    private MyListViewAdapter mListViewAdapter;
    private List<Bean>        mMedicineBeanList   = new ArrayList<Bean>();
    private List<String>      mMedicineNames      = new ArrayList<>();
    private List<String>      mMedicineAttentions = new ArrayList<>();

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_one, null);
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //LoadMenu();
         mMedicineNames =((DoctorBottomActivity) Objects.requireNonNull(getActivity())).getmMedicineNames();
         mMedicineAttentions = ((DoctorBottomActivity) Objects.requireNonNull(getActivity())).getmMedicineAttentions();
        mEditText = getActivity().findViewById(edit_medicine);      //edit下拉列表
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
            //  ShowListPopulWindow();
            }
        });

        //加载listview
        mListView = getActivity().findViewById(R.id.listView);
        mListViewAdapter = new MyListViewAdapter(getActivity(), mMedicineBeanList);
        mListView.setAdapter(mListViewAdapter);

        //添加一个药物的点击事件
        final Button saveButton = getActivity().findViewById(R.id.add);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                SaveMedicineMessage();
            }
        });

        //点击某一个药物跳转到详情页面
        mListView.setOnItemClickListener((adapterView, view, position, id) -> {
            Bean medicineBean = mMedicineBeanList.get(position);
            String medicinename = medicineBean.getName();
            String medicinecontent = medicineBean.getContent();
            String medicineattention = medicineBean.getAttention();
            String medicinetime = medicineBean.gettime();
            Intent intent = new Intent(getActivity(), MedicineDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putCharSequence("position", position + "");
            bundle.putCharSequence("medicinename", medicinename);
            bundle.putCharSequence("medicinecontent", medicinecontent);
            bundle.putCharSequence("medicineattention", medicineattention);
            bundle.putCharSequence("medicinetime", medicinetime);
            intent.putExtras(bundle);
            startActivityForResult(intent, 1000);
        });
    }

    //接收制定计划界面的返回数据
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == 1001) {
            int nowposition = Integer.valueOf(data.getStringExtra("reposition")).intValue();
            Bean medicineBean = mMedicineBeanList.get(nowposition);
            medicineBean.setattention(data.getStringExtra("remedicineattention"));
            medicineBean.settime(data.getStringExtra("remedicinetime"));
            medicineBean.setcontent(data.getStringExtra("remedicinenum"));
        }
    }

    //下拉列表内容的获取
    public void LoadMenu () {
        Thread thread=new Thread(() -> {
            Socket socket;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("request_type", "13");
                jsonObject.put("base_type", "med");
                socket = SocketUtil.getSendSocket();
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(jsonObject.toString());
                out.close();

                Thread.sleep(4000);

                socket = SocketUtil.getArraySendSocket();
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
                    mMedicineNames.add(jsonArray.getJSONObject(i).getString("medicineName"));
                    mMedicineAttentions.add(jsonArray.getJSONObject(i).getString
                            ("medicineRemarks"));
                    //添加一个获取注意事项
                }
                socket.close();
            } catch (JSONException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
            Thread.interrupted();
        });
        thread.start();
    }

    //edit下拉列表
    private void ShowListPopulWindow () {
        int size = mMedicineNames.size();
        String[] mediciness = (String[]) mMedicineNames.toArray(new String[size]);
        final ListPopupWindow listPopupWindow;
        listPopupWindow = new ListPopupWindow(getActivity());
        listPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout
                .simple_list_item_1, mediciness));//用android内置布局，或设计自己的样式
        listPopupWindow.setAnchorView(mEditText);//以哪个控件为基准，在该处以mEditText为基准
        listPopupWindow.setModal(true);

        //设置项点击监听
        listPopupWindow.setOnItemClickListener((adapterView, view, i, l) -> {
            mEditText.setText(mediciness[i]);//把选择的选项内容展示在EditText上
            listPopupWindow.dismiss();//如果已经选择了，隐藏起来
        });
        listPopupWindow.show();//把ListPopWindow展示出来
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
                        mMedicineBeanList.remove(position);
                        ((DoctorNursingPlanFragment) (DoctorOneFragment.this.getParentFragment())
                        ).ChangemPlannum(-1);
                        mListViewAdapter.notifyDataSetChanged();
                        ((DoctorNursingPlanFragment) (DoctorOneFragment.this.getParentFragment())
                        ).deletemAllMedicineItem(position);
                    }
                }).show();
    }

    //保存添加的药物到列表
    private void SaveMedicineMessage () {
        EditText nameEditText = getActivity().findViewById(edit_medicine);

        if (StringUtils.isEmpty(nameEditText.getText().toString())) {
            Toast.makeText(getActivity(), "药品名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        int size = mMedicineNames.size();
        String[] mediciness = (String[]) mMedicineNames.toArray(new String[size]);
        int size2 = mMedicineAttentions.size();
        String[] medicineattention = (String[]) mMedicineAttentions.toArray(new String[size2]);

        String mAttention = "";
        for (int j = 0; j < size; j++) {
            if (nameEditText.getText().toString().equals(mediciness[j])) {
                mAttention = medicineattention[j];
            }
        }

        //判断该药品是否存在
        for (Bean medicineBean : mMedicineBeanList)   //添加了的药品列表
        {
            if (StringUtils.equals(medicineBean.getName(), nameEditText.getText().toString())) {
                Toast.makeText(getActivity(), nameEditText.getText().toString() + "已经存在", Toast
                        .LENGTH_SHORT).show();
                return;
            }
            //判断药品名和药品库名中的药品名是否相同，相同则使attention为对应的attention
        }

        Bean medicineBean = new Bean(nameEditText.getText().toString());
        medicineBean.setattention(mAttention);
        medicineBean.setcontent("");
        medicineBean.settime("000000");
        mMedicineBeanList.add(medicineBean);
        ((DoctorNursingPlanFragment) (DoctorOneFragment.this.getParentFragment())).ChangemPlannum
                (1);
        ((DoctorNursingPlanFragment) (DoctorOneFragment.this.getParentFragment()))
                .setmAllMedicineItem(medicineBean);
        mListViewAdapter.notifyDataSetChanged();
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