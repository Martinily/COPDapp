package com.funnyseals.app.feature.doctorNursingPlan;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
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

import static com.funnyseals.app.R.id.edit_instrument;

/**

 */
public class DoctorTwoFragment extends Fragment {
    private        EditText        mEditText;
    /**
     * Context
     */
    private        Context         mContext;
    /**
     * listview
     */
    private        ListView        listView;
    /**
     * 适配器
     */
    private        ListViewAdapter listViewAdapter;
    private static Connection      conn;
    /**
     * 保存数据
     */
    private        List<Bean>      instrumentBeanList = new ArrayList<Bean>();
    private        List<String>    InstrumentNames;
    private        Thread          mThread;
    //用于执行数据库线程
    @SuppressLint("HandlerLeak")
    private        Handler         mHandler           = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                InstrumentNames = (List<String>) msg.obj;
            }
        }
    };
    private        ImageView       imageView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_two, null);
        if (mThread == null) {
            mThread = new Thread(runnable);
            mThread.start();
        }
        return view;
    }

    Runnable runnable = () -> {
        try {
            conn = UserDao.getConnection();
            if (conn != null) {
                PreparedStatement statement = conn.prepareStatement("SELECT QXK_QXMC FROM qxk");
                ResultSet rs = statement.executeQuery();
                InstrumentNames = new ArrayList<>();
                while (rs.next()) {
                    InstrumentNames.add(rs.getString("QXK_QXMC"));
                }
                Message message = Message.obtain();
                message.what = 0;
                message.obj = InstrumentNames;
                mHandler.sendMessage(message);
                conn.close();
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mEditText = getActivity().findViewById(edit_instrument);      //edit下拉列表
        mEditText.setOnTouchListener((view, event) -> {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getX() >= (mEditText.getWidth() - mEditText
                        .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                    mEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_nadown), null);
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

        //this.mContext = this;
        //加载listview
        listView = getActivity().findViewById(R.id.listViewinstrument);
        listViewAdapter = new ListViewAdapter(getActivity(), instrumentBeanList);
        listView.setAdapter(listViewAdapter);

        //save button的点击事件
        Button saveButton = getActivity().findViewById(R.id.addinstrument);
        saveButton.setOnClickListener(v -> saveinstrumentMessage());

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            Bean instrumentBean = instrumentBeanList.get(position);
            String instrumentname = instrumentBean.getName();
            Intent intent = new Intent(getActivity(), InstrumentDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putCharSequence("instrumentname", instrumentname);
            intent.putExtras(bundle);
            startActivity(intent);

        });
    }

    private void showListPopulWindow() {    //edit下拉列表
        final ListPopupWindow listPopupWindow;
        listPopupWindow = new ListPopupWindow(getActivity());
        listPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, InstrumentNames));//用android内置布局，或设计自己的样式
        listPopupWindow.setAnchorView(mEditText);//以哪个控件为基准，在该处以mEditText为基准
        listPopupWindow.setModal(true);

        //设置项点击监听
        listPopupWindow.setOnItemClickListener((adapterView, view, i, l) -> {
            mEditText.setText(InstrumentNames.get(i));//把选择的选项内容展示在EditText上
            listPopupWindow.dismiss();//如果已经选择了，隐藏起来
        });
        listPopupWindow.show();//把ListPopWindow展示出来
        listPopupWindow.setOnDismissListener(() -> mEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_na), null));
    }

    /**
     * 保存信息
     */
    private void saveinstrumentMessage() {
        EditText nameEditText = getActivity().findViewById(edit_instrument);

        if (StringUtils.isEmpty(nameEditText.getText().toString())) {
            Toast.makeText(getActivity(), "器械名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //判断该器械是否存在
        for (Bean instrumentBean : instrumentBeanList) //添加了的器械列表
        {
            if (StringUtils.equals(instrumentBean.getName(), nameEditText.getText().toString())) {
                Toast.makeText(getActivity(), nameEditText.getText().toString() + "已经存在", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Bean instrumentBean = new Bean(nameEditText.getText().toString());
        instrumentBeanList.add(instrumentBean);
        listViewAdapter.notifyDataSetChanged();
    }
}
