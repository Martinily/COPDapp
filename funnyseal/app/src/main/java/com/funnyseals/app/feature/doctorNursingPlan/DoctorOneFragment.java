package com.funnyseals.app.feature.doctorNursingPlan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.funnyseals.app.model.UserDao;
import org.apache.commons.lang3.StringUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static com.funnyseals.app.R.id.edit_medicine;
/*
护理计划Onefragment,about medicine
 */
public class DoctorOneFragment extends Fragment {
    private        EditText        mEditText;
    private static Connection      CONN;
    private        Context         mContext;
    private        ListView        mListView;
    private        MyListViewAdapter mListViewAdapter;
    private        List<Bean>      mMedicineBeanList = new ArrayList<Bean>();
    private        List<String>    mMedicineNames;
    private        Thread          mThread;
    private int mMedicinesave = 0;
    //用于执行数据库线程
    private        Handler         mHandler          = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mMedicineNames = (List<String>) msg.obj;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_one, null);
        if (mThread == null) {
            mThread = new Thread(runnable);
            mThread.start();
        }
        return view;
    }

    private Runnable runnable = () -> {
        try {
            CONN = UserDao.getConnection();
            if (CONN != null) {
                PreparedStatement statement = CONN.prepareStatement("SELECT YWK_YWMC FROM ywk");
                ResultSet rs = statement.executeQuery();
                mMedicineNames = new ArrayList<>();
                while (rs.next()) {
                    mMedicineNames.add(rs.getString("YWK_YWMC"));
                }
                Message message = Message.obtain();
                message.what = 0;
                message.obj = mMedicineNames;
                mHandler.sendMessage(message);
                CONN.close();
                rs.close();
                statement.close();
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
                    mEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_expand_less_black_24dp), null);
                    showListPopulWindow();
                    return true;
                }
            }
            return false;
        });
        mEditText.setOnFocusChangeListener((view, b) -> {
            if (b) {
                showListPopulWindow();
            }
        });

        //加载listview
        mListView = getActivity().findViewById(R.id.listView);
        mListViewAdapter = new MyListViewAdapter(getActivity(), mMedicineBeanList);
        mListView.setAdapter(mListViewAdapter);

        //save button的点击事件
        final Button saveButton = getActivity().findViewById(R.id.add);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMedicinesave==0)
                    saveMedicineMessage();
                else
                    saveButton.setEnabled(false);
            }
        });

        mListView.setOnItemClickListener((adapterView, view, position, id) -> {
            if(mMedicinesave==0) {
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
            }
        });

        final Button medicinesaveall = (Button) getActivity().findViewById(R.id.saveallmedicine);
        medicinesaveall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setTitle("我的提示").setMessage("确定要保存吗？保存后不可更改")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(getActivity(), "已保存", Toast.LENGTH_SHORT).show();
                                medicinesaveall.setTextColor(0xFFD0EFC6);
                                medicinesaveall.setEnabled(false);
                                // ((MainActivity)getActivity()).setmtestmedicine("1");
                                mMedicinesave=1;
                            }
                        }).show();
            }
        });
    }

    //接收制定计划界面的返回数据
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == 1001) {
            int nowposition = Integer.valueOf(data.getStringExtra("reposition")).intValue();
            Bean medicineBean = mMedicineBeanList.get(nowposition);
            medicineBean.setattention(data.getStringExtra("remedicineattention"));
            medicineBean.settime(data.getStringExtra("remedicinetime"));
            medicineBean.setcontent(data.getStringExtra("remedicinenum"));
        }
    }

    /*
     *edit下拉列表
     */
    private void showListPopulWindow() {
        final ListPopupWindow listPopupWindow;
        listPopupWindow = new ListPopupWindow(getActivity());
        listPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mMedicineNames));//用android内置布局，或设计自己的样式
        listPopupWindow.setAnchorView(mEditText);//以哪个控件为基准，在该处以mEditText为基准
        listPopupWindow.setModal(true);

        //设置项点击监听
        listPopupWindow.setOnItemClickListener((adapterView, view, i, l) -> {
            mEditText.setText(mMedicineNames.get(i));//把选择的选项内容展示在EditText上
            listPopupWindow.dismiss();//如果已经选择了，隐藏起来
        });
        //listPopupWindow.show();//把ListPopWindow展示出来

        listPopupWindow.setOnDismissListener(() -> mEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_expand_more_black_24dp), null));
    }

    public void showInfo(final int position) {
        new AlertDialog.Builder(getActivity()).setTitle("我的提示").setMessage("确定要删除吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.err.print("111");
                        mMedicineBeanList.remove(position);
                        mListViewAdapter.notifyDataSetChanged();
                    }
                }).show();
    }

    /**
     * 保存信息
     */
    private void saveMedicineMessage() {
        EditText nameEditText = getActivity().findViewById(edit_medicine);

        if (StringUtils.isEmpty(nameEditText.getText().toString())) {
            Toast.makeText(getActivity(), "药品名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //判断该药品是否存在
        for (Bean medicineBean : mMedicineBeanList)   //添加了的药品列表
        {
            if (StringUtils.equals(medicineBean.getName(), nameEditText.getText().toString())) {
                Toast.makeText(getActivity(), nameEditText.getText().toString() + "已经存在", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Bean medicineBean = new Bean(nameEditText.getText().toString());
        mMedicineBeanList.add(medicineBean);
        ((DoctorNursingPlanFragment)(DoctorOneFragment.this.getParentFragment())).setAllMedicineItem(medicineBean);
        mListViewAdapter.notifyDataSetChanged();
    }

    public class MyListViewAdapter extends BaseAdapter {
        /**
         * Context
         */
        private Context mContext;

        /**
         * 数据
         */
        private String name;
        private List<Bean> BeanList;

        /**
         * 构造函数
         */
        public MyListViewAdapter(Context context, List<Bean> BeanList) {
            this.mContext = context;
            this.BeanList = BeanList;
        }

        @Override
        public int getCount() {
            return BeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
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
                public void onClick(View v) {
                    if(mMedicinesave==0) {
                        deleteButtonAction(removePosition);
                        mListViewAdapter.notifyDataSetChanged();
                    }
                    else
                        deleteButton.setEnabled(false);
                }
            });
            return view;
        }

        public void deleteButtonAction(int position) {
            showInfo(position);
            notifyDataSetChanged();
        }

        public String getName() {
            return name;
        }
    }
}