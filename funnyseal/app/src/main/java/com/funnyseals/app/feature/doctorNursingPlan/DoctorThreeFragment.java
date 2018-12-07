package com.funnyseals.app.feature.doctorNursingPlan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.Toast;
import com.funnyseals.app.R;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.List;
import static com.funnyseals.app.R.id.edit_sports;

/**
 * 护理计划three fragment,about sports
 */
public class DoctorThreeFragment extends Fragment {
    private EditText mEditText;
    private Context  mContext;
    private ListView mListView;
    private ListViewAdapter mListViewAdapter;
    private List<Bean> mSportsBeanList = new ArrayList<Bean>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_three, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mEditText = getActivity().findViewById(edit_sports);
        mEditText.setOnTouchListener((view, event) -> {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getX() >= (mEditText.getWidth() - mEditText
                        .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    //my action here
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
        mListView = getActivity().findViewById(R.id.listViewsports);
        mListViewAdapter = new ListViewAdapter(getActivity(), mSportsBeanList);
        mListView.setAdapter(mListViewAdapter);

        //save button的点击事件
        Button saveButton = getActivity().findViewById(R.id.addsports);
        saveButton.setOnClickListener(v -> savesportsMessage());

        mListView.setOnItemClickListener((adapterView, view, position, id) -> {
            Bean sportsBean = mSportsBeanList.get(position);
            String sportsname = sportsBean.getName();
            Intent intent = new Intent(getActivity(), SportsDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putCharSequence("sportsname", sportsname);
            intent.putExtras(bundle);
            startActivity(intent);

        });
    }

    /**
     * 下拉列表
     */
    private void showListPopulWindow() {
        final String[] list = {"跑步", "游泳", "羽毛球", "快走"};
        final ListPopupWindow listPopupWindow;
        listPopupWindow = new ListPopupWindow(getActivity());
        listPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list));
        //以哪个控件为基准，在该处以mEditText为基准
        listPopupWindow.setAnchorView(mEditText);
        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener((adapterView, view, i, l) -> {
            mEditText.setText(list[i]);
            listPopupWindow.dismiss();
        });
        listPopupWindow.show();
        listPopupWindow.setOnDismissListener(() -> mEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_expand_more_black_24dp), null));
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
        for (Bean sportsBean : mSportsBeanList) {
            if (StringUtils.equals(sportsBean.getName(), nameEditText.getText().toString())) {
                Toast.makeText(getActivity(), nameEditText.getText().toString() + "已经存在", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Bean sportsBean = new Bean(nameEditText.getText().toString());
        mSportsBeanList.add(sportsBean);

        mListViewAdapter.notifyDataSetChanged();
    }
}
