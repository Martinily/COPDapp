package com.funnyseals.app.feature.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnyseals.app.R;
import com.funnyseals.app.model.UserTemp;

import java.util.List;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/11/30
 *     desc   :患者列表adapter
 *     version: 1.0
 * </pre>
 */
public class PatientListItemAdapterTemp extends BaseAdapter {
    private LayoutInflater mInflater;

    private List<UserTemp> mUserTemps;

    public PatientListItemAdapterTemp(Context context, List<UserTemp> userTemps) {
        this.mInflater = LayoutInflater.from(context);
        this.mUserTemps = userTemps;
    }

    @Override
    public int getCount() {
        return mUserTemps.size();
    }

    @Override
    public UserTemp getItem(int position) {
        return mUserTemps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PatientListItemAdapterTemp.ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_patient, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.mPortrait = convertView.findViewById(R.id.patient_item_portrait);
            viewHolder.mName = convertView.findViewById(R.id.patient_item_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.init(getItem(position));

        return convertView;
    }

    private class ViewHolder {

        private ImageView mPortrait;

        private TextView mName;

        private void init(UserTemp item) {

            mPortrait.setBackgroundResource(item.getPortrait());
            mName.setText(item.getName());
        }
    }
}
