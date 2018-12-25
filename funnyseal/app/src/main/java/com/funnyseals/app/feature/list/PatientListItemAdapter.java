package com.funnyseals.app.feature.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnyseals.app.R;
import com.funnyseals.app.custom_view.Portrait;
import com.funnyseals.app.model.User;

import java.util.List;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/11/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PatientListItemAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    private List<User> mUsers;

    public PatientListItemAdapter (Context context, List<User> users) {
        this.mInflater = LayoutInflater.from(context);
        this.mUsers = users;
    }

    @Override
    public int getCount () {
        return mUsers.size();
    }

    @Override
    public User getItem (int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    /*
    列表内容
     */
    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_patient, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.mimageView = convertView.findViewById(R.id.patient_item_image);
            viewHolder.mName = convertView.findViewById(R.id.patient_item_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.init(getItem(position));

        return convertView;
    }

    private class ViewHolder {

        private ImageView mimageView;

        private TextView mName;

        private void init (User user) {
            mimageView.setImageResource(R.mipmap.portrait0);
            mName.setText(user.getName());
        }
    }
}
