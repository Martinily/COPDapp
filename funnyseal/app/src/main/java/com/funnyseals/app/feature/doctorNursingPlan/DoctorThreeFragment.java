package com.funnyseals.app.feature.doctorNursingPlan;

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
    private MyListViewAdapter mListViewAdapter;
    private List<Bean> mSportsBeanList = new ArrayList<Bean>();
    private int mSportssave = 0;

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
        mListViewAdapter = new MyListViewAdapter(getActivity(), mSportsBeanList);
        mListView.setAdapter(mListViewAdapter);

        //save button的点击事件
        final Button saveButton = getActivity().findViewById(R.id.addsports);
        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mSportssave==0) {
                    savesportsMessage();
                }
                else
                    saveButton.setEnabled(false);
            }
        });

        mListView.setOnItemClickListener((adapterView, view, position, id) -> {
            if(mSportssave==0) {
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
            }
        });
        final Button sportssaveall = (Button) getActivity().findViewById(R.id.saveallsports);
        sportssaveall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setTitle("我的提示").setMessage("确定要保存吗？保存后不可更改")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), "已保存", Toast.LENGTH_SHORT).show();
                                sportssaveall.setTextColor(0xFFD0EFC6);
                                sportssaveall.setEnabled(false);
                                mSportssave=1;
                                //((MainActivity)getActivity()).setmtestsports("1");
                            }
                        }).show();
            }
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

    public void showInfo(final int position) {
        new AlertDialog.Builder(getActivity()).setTitle("我的提示").setMessage("确定要删除吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSportsBeanList.remove(position);
                        mListViewAdapter.notifyDataSetChanged();
                    }
                }).show();
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
        //((MainActivity)getActivity()).setmtestsports("0");
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
                    if(mSportssave==0){
                        deleteButtonAction(removePosition);
                        mListViewAdapter.notifyDataSetChanged();
                    }
                    else {
                        deleteButton.setEnabled(false);
                    }
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
