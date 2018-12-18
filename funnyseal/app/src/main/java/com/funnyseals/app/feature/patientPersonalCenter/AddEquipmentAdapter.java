package com.funnyseals.app.feature.patientPersonalCenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnyseals.app.R;
import com.funnyseals.app.model.AddEquipment;

import java.util.List;

public class AddEquipmentAdapter extends ArrayAdapter {
    private final int resourceId;
    public AddEquipmentAdapter(Context context, int textViewResourceId, List<AddEquipment> objects) {
        super(context, textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AddEquipment addEquipment = (AddEquipment) getItem(position); // 获取当前项的AddEquipment实例
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        TextView EquipmentName = view.findViewById(R.id.add_equipment_name);//获取该布局内的文本视图
        TextView EquipmentState = view.findViewById(R.id.add_equipment_state);//获取该布局内的文本视图

        EquipmentState.setText(addEquipment.getEquipment_state());
        EquipmentName.setText(addEquipment.getEquipment_name());
        return view;
    }


}
