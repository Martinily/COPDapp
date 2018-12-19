package com.funnyseals.app.feature.patientPersonalCenter;

import com.funnyseals.app.model.AddEquipment;

import org.json.JSONException;
import org.json.JSONObject;
    /**
     *我的设备信息
     * 获取部分
     */
public class findAddEquipment {
    public String equipment_name,equipment_state;
    public String getEquipment_name(){
        return equipment_name;
    }
    public String getEquipment_state(){
        return equipment_state;
    }
    public void setEquipment_name(String equipment_name){
        this.equipment_name=equipment_name;
    }
    public void setEquipment_state(String equipment_state){
        this.equipment_state=equipment_state;
    }
    public findAddEquipment(String equipment_name,String equipment_state){
        this.equipment_name=equipment_name;
        this.equipment_state=equipment_state;
    }

    public static AddEquipment sectionData(JSONObject json){
        try {
            return new AddEquipment(
                    json.getString("eName"),
                    json.getString("eState"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}


