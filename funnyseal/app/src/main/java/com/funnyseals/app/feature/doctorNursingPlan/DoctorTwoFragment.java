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
 *护理计划two fragment, about instrument
 */
public class DoctorTwoFragment extends Fragment {
    private        EditText        mEditText;
    private        Context         mContext;
    private        ListView        mListView;
    private        ListViewAdapter mListViewAdapter;
    private static Connection      CONN;
    private        List<Bean>      mInstrumentBeanList = new ArrayList<Bean>();
    private        List<String>    mInstrumentNames;
    private        Thread          mThread;

    //用于执行数据库线程
    @SuppressLint("HandlerLeak")
    private Handler   mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mInstrumentNames = (List<String>) msg.obj;
            }
        }
    };
    private ImageView imageView;

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
            CONN = UserDao.getConnection();
            if (CONN != null) {
                PreparedStatement statement = CONN.prepareStatement("SELECT QXK_QXMC FROM qxk");
                ResultSet rs = statement.executeQuery();
                mInstrumentNames = new ArrayList<>();
                while (rs.next()) {
                    mInstrumentNames.add(rs.getString("QXK_QXMC"));
                }
                Message message = Message.obtain();
                message.what = 0;
                message.obj = mInstrumentNames;
                mHandler.sendMessage(message);
                CONN.close();
                rs.close();
                statement.close();
            }
            else {
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

        //加载listview
        mListView = getActivity().findViewById(R.id.listViewinstrument);
        mListViewAdapter = new ListViewAdapter(getActivity(), mInstrumentBeanList);
        mListView.setAdapter(mListViewAdapter);

        //save button的点击事件
        Button saveButton = getActivity().findViewById(R.id.addinstrument);
        saveButton.setOnClickListener(v -> saveinstrumentMessage());

        mListView.setOnItemClickListener((adapterView, view, position, id) -> {
            Bean instrumentBean = mInstrumentBeanList.get(position);
            String instrumentname = instrumentBean.getName();
            Intent intent = new Intent(getActivity(), InstrumentDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putCharSequence("instrumentname", instrumentname);
            intent.putExtras(bundle);
            startActivity(intent);

        });
    }

    /*
    *展示下拉列表
     */
    private void showListPopulWindow() {
        final ListPopupWindow listPopupWindow;
        listPopupWindow = new ListPopupWindow(getActivity());
        listPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mInstrumentNames));
        listPopupWindow.setAnchorView(mEditText);
        listPopupWindow.setModal(true);

        //设置项点击监听
        listPopupWindow.setOnItemClickListener((adapterView, view, i, l) -> {
            mEditText.setText(mInstrumentNames.get(i));
            listPopupWindow.dismiss();
        });
        listPopupWindow.show();
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
        for (Bean instrumentBean : mInstrumentBeanList) {
            if (StringUtils.equals(instrumentBean.getName(), nameEditText.getText().toString())) {
                Toast.makeText(getActivity(), nameEditText.getText().toString() + "已经存在", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Bean instrumentBean = new Bean(nameEditText.getText().toString());
        mInstrumentBeanList.add(instrumentBean);
        mListViewAdapter.notifyDataSetChanged();
    }
}