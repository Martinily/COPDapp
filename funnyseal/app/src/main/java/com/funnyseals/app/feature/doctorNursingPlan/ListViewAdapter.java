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
 *     desc   :详细内容添加的列表adapter
 *     version: 1.0
 * </pre>
 */
public class ListViewAdapter extends BaseAdapter {

    private Context mContext;
    private String     mName;
    private List<Bean> mBeanList;

    public ListViewAdapter(Context context, List<Bean> BeanList) {
        this.mContext = context;
        this.mBeanList = BeanList;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return mBeanList.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *Position of the item whose data we want within the adapter'sdata set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return null;
    }

    /**
     * Get the row id associated with the specified position in the list.
     *The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * detail listview
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = View.inflate(mContext, R.layout.listview, null);
        }

        Bean bean = mBeanList.get(position);
        if (bean == null) {
            bean = new Bean("NoName");
        }

        //更新数据
        final TextView nameTextView = view.findViewById(R.id.showName);
        nameTextView.setText(bean.getName());

        final int removePosition = position;

        //删除按钮点击事件
        Button deleteButton = view.findViewById(R.id.showDeleteButton);
        deleteButton.setOnClickListener(v -> {
            Toast.makeText(mContext, "已删除", Toast.LENGTH_SHORT).show();
            deleteButtonAction(removePosition);
        });
        return view;
    }
    //删除按钮操作
    private void deleteButtonAction(int position) {
        mBeanList.remove(position);
        notifyDataSetChanged();
    }

    public String getName() {
        return mName;
    }
}