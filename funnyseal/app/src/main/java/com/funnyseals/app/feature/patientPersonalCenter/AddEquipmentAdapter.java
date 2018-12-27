package com.funnyseals.app.feature.patientPersonalCenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.funnyseals.app.R;
import com.funnyseals.app.model.AddEquipment;

import java.util.List;

/**
 * 患者端
 * 适配器类
 */
public class AddEquipmentAdapter extends ArrayAdapter {
    private final int resourceId;
    private List<AddEquipment> myEquipment;

    public AddEquipmentAdapter (Context context, int textViewResourceId, List<AddEquipment>
            objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    /**
     * Notifies the attached observers that the underlying data has been changed
     * and any View reflecting the data set should refresh itself.
     */

    /**
     * 获取当前项的AddEquipment实例
     * /实例化一个对象
     * 获取该布局内的文本视图
     * 获取该布局内的文本视图
     * 设置文本
     */
    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        AddEquipment addEquipment = (AddEquipment) getItem(position);
        @SuppressLint("ViewHolder")
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView EquipmentName = view.findViewById(R.id.add_equipment_name);
        TextView EquipmentState = view.findViewById(R.id.add_equipment_state);
        ImageButton EquipmentDelete=view.findViewById(R.id.add_equipment_delete);

        final int removePosition = position;
        EquipmentDelete.setOnClickListener(v -> {
            deleteButtonAction(removePosition);
        });

        EquipmentState.setText(addEquipment.getEquipment_state());
        EquipmentName.setText(addEquipment.getEquipment_name());
        return view;
    }
    //删除按钮操作
    private void deleteButtonAction (int position) {
        myEquipment.remove(position);
        notifyDataSetChanged();
    }


}
