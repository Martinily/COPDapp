package com.funnyseals.app.feature.patientPersonalCenter;

import com.funnyseals.app.model.AddEquipment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 我的设备信息
 * 获取部分
 */
public class findAddEquipment {
    public String equipment_name, equipment_state;
    public int imageId;

    public findAddEquipment (String equipment_name, String equipment_state,int imageId) {
        this.equipment_name = equipment_name;
        this.equipment_state = equipment_state;
        this.imageId=imageId;
    }

    public static AddEquipment sectionData (JSONObject json) {
        try {
            return new AddEquipment(
                    json.getString("eName"),
                    json.getString("eState"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getEquipment_name () {
        return equipment_name;
    }

    public int getImageId(){
        return imageId;
    }
    public void setImageId(int imageId){
        this.imageId=imageId;
    }

    public void setEquipment_name (String equipment_name) {
        this.equipment_name = equipment_name;
    }

    public String getEquipment_state () {
        String eState="";
        if (equipment_state.equals("0"))
        {
            eState="未使用";
        }
        if (equipment_state.equals("1")){
            eState="使用";
        }
        return eState;
    }

    public void setEquipment_state (String equipment_state) {
        this.equipment_state = equipment_state;
    }

}


