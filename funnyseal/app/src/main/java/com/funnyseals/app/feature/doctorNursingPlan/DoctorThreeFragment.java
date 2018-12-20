package com.funnyseals.app.feature.doctorNursingPlan;

import android.app.AlertDialog;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.funnyseals.app.R.id.edit_sports;

/**
 * 护理计划three fragment,about sports
 */
public class DoctorThreeFragment extends Fragment {
    private EditText mEditText;
    private ListView mListView;
    private MyListViewAdapter mListViewAdapter;
    private List<Bean> mSportsBeanList = new ArrayList<Bean>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor_three, null);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mEditText = Objects.requireNonNull(getActivity()).findViewById(edit_sports);
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
        mListView = getActivity().findViewById(R.id.listViewsports);
        mListViewAdapter = new MyListViewAdapter(getActivity(), mSportsBeanList);
        mListView.setAdapter(mListViewAdapter);

        //save button的点击事件
        final Button saveButton = getActivity().findViewById(R.id.addsports);
        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                    SavesportsMessage();
            }
        });

        mListView.setOnItemClickListener((adapterView, view, position, id) -> {
                Bean sportsBean = mSportsBeanList.get(position);
                String sportsname = sportsBean.getName();
                String sportscontent = sportsBean.getContent();
                String sportsattention = sportsBean.getAttention();
                Intent intent = new Intent(getActivity(), SportsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putCharSequence("position", position + "");
                bundle.putCharSequence("sportsname", sportsname);
                bundle.putCharSequence("sportscontent", sportscontent);
                bundle.putCharSequence("sportsattention", sportsattention);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1000);
        });
    }

    //接收返回数据
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == 1001)
        {
            int nowposition=Integer.valueOf(data.getStringExtra("reposition")).intValue();
            Bean sportsBean = mSportsBeanList.get(nowposition);
            sportsBean.setattention(data.getStringExtra("resportsattention"));
            sportsBean.setcontent(data.getStringExtra("resportsnum"));
        }
    }

     //edit下拉列表
    private void ShowListPopulWindow() {
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
        listPopupWindow.setOnDismissListener(() -> mEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_na), null));
    }

    //删除已添加药物
    public void ShowInfo(final int position) {
        new AlertDialog.Builder(getActivity()).setTitle("我的提示").setMessage("确定要删除吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSportsBeanList.remove(position);
                        ((DoctorNursingPlanFragment)(DoctorThreeFragment.this.getParentFragment())).ChangemPlannum(-1);
                        mListViewAdapter.notifyDataSetChanged();
                        ((DoctorNursingPlanFragment)(DoctorThreeFragment.this.getParentFragment())).deletemAllSportsItem(position);
                    }
                }).show();
    }

     //保存添加的运动到列表
    private void SavesportsMessage() {
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
        sportsBean.setcontent("");
        sportsBean.setattention("");
        mSportsBeanList.add(sportsBean);
        ((DoctorNursingPlanFragment)(DoctorThreeFragment.this.getParentFragment())).ChangemPlannum(1);
        ((DoctorNursingPlanFragment)(DoctorThreeFragment.this.getParentFragment())).setmAllSportsItem(sportsBean);
        //((MainActivity)getActivity()).setmtestsports("0");
        mListViewAdapter.notifyDataSetChanged();
    }

    //列表适配器
    public class MyListViewAdapter extends BaseAdapter {

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
                        deleteButtonAction(removePosition);
                        mListViewAdapter.notifyDataSetChanged();
                }
            });
            return view;
        }

        public void deleteButtonAction(int position) {
            ShowInfo(position);
            notifyDataSetChanged();
        }

        public String getName() {
            return name;
        }
    }
}