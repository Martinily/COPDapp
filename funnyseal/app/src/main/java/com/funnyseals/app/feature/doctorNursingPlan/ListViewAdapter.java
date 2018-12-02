package com.funnyseals.app.feature.doctorNursingPlan;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.funnyseals.app.R;

import java.util.List;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/11/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ListViewAdapter extends BaseAdapter {
    /**
     * Context
     */
    private Context mContext;

    /**
     * 数据
     */
    private String     name;
    private List<Bean> BeanList;

    /**
     * 构造函数
     */
    public ListViewAdapter(Context context, List<Bean> BeanList) {
        this.mContext = context;
        this.BeanList = BeanList;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return BeanList.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return null;
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = View.inflate(mContext, R.layout.listview, null);
        }

        Bean bean = BeanList.get(position);
        if (bean == null) {
            bean = new Bean("NoName");
        }

        //更新数据
        final TextView nameTextView = view.findViewById(R.id.showName);
        nameTextView.setText(bean.getName());

        final int removePosition = position;

        //删除按钮点击事件
        Button deleteButton = view.findViewById(R.id.showDeleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "已删除", Toast.LENGTH_SHORT).show();
                deleteButtonAction(removePosition);
            }
        });


        return view;
    }

    private void deleteButtonAction(int position) {
        BeanList.remove(position);

        notifyDataSetChanged();
    }

    public String getName() {
        return name;
    }
}
