package com.funnyseals.app.feature.doctorNursingPlan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.funnyseals.app.R;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.funnyseals.app.R.id.edit_sports;

/**
 */
public class DoctorThreeFragment extends Fragment {
    private EditText mEditText;
    /**
     * Context
     */
    private Context  mContext;
    /**
     * listview
     */
    private ListView listView;

    /**
     * 适配器
     */
    private ListViewAdapter listViewAdapter;

    /**
     * 保存数据
     */
    private List<Bean> sportsBeanList = new ArrayList<Bean>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_three, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mEditText = getActivity().findViewById(edit_sports);      //edit下拉列表
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
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
            }
        });
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showListPopulWindow();
                }
            }
        });

        //this.mContext = this;
        //加载listview
        listView = getActivity().findViewById(R.id.listViewsports);
        listViewAdapter = new ListViewAdapter(getActivity(), sportsBeanList);
        listView.setAdapter(listViewAdapter);

        //save button的点击事件
        Button saveButton = getActivity().findViewById(R.id.addsports);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savesportsMessage();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Bean sportsBean = sportsBeanList.get(position);
                String sportsname = sportsBean.getName();
                Intent intent = new Intent(getActivity(), SportsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putCharSequence("sportsname", sportsname);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }


    private void showListPopulWindow() {    //edit下拉列表
        final String[] list = {"跑步", "游泳", "羽毛球", "快走"};//要填充的数据
        final ListPopupWindow listPopupWindow;
        listPopupWindow = new ListPopupWindow(getActivity());
        listPopupWindow.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list));//用android内置布局，或设计自己的样式
        listPopupWindow.setAnchorView(mEditText);//以哪个控件为基准，在该处以mEditText为基准
        listPopupWindow.setModal(true);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置项点击监听
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mEditText.setText(list[i]);//把选择的选项内容展示在EditText上
                listPopupWindow.dismiss();//如果已经选择了，隐藏起来
            }
        });
        listPopupWindow.show();//把ListPopWindow展示出来
        listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_na), null);
            }
        });
    }

    /**
     * 保存信息
     */
    private void savesportsMessage() {
        EditText nameEditText = getActivity().findViewById(edit_sports);

        if (StringUtils.isEmpty(nameEditText.getText().toString())) {
            Toast.makeText(getActivity(), "运动名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //判断该运动是否存在
        for (Bean sportsBean : sportsBeanList)   //添加了的运动列表
        {
            if (StringUtils.equals(sportsBean.getName(), nameEditText.getText().toString())) {
                Toast.makeText(getActivity(), nameEditText.getText().toString() + "已经存在", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Bean sportsBean = new Bean(nameEditText.getText().toString());
        sportsBeanList.add(sportsBean);

        listViewAdapter.notifyDataSetChanged();
    }
}
