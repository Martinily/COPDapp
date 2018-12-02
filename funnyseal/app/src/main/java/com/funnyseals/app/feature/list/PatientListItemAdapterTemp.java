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
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PatientListItemAdapterTemp extends BaseAdapter {
    private LayoutInflater inflater;

    private List<UserTemp> userTemps;

    public PatientListItemAdapterTemp(Context context, List<UserTemp> userTemps) {
        this.inflater = LayoutInflater.from(context);
        this.userTemps = userTemps;
    }

    @Override
    public int getCount() {
        return userTemps.size();
    }

    @Override
    public UserTemp getItem(int position) {
        return userTemps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PatientListItemAdapterTemp.ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_patient, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.portrait = convertView.findViewById(R.id.patient_item_portrait);
            viewHolder.name = convertView.findViewById(R.id.patient_item_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.init(getItem(position));

        return convertView;
    }

    private class ViewHolder {

        private ImageView portrait;

        private TextView name;

        private void init(UserTemp item) {

            portrait.setBackgroundResource(item.getPortrait());
            name.setText(item.getName());
        }
    }
}
