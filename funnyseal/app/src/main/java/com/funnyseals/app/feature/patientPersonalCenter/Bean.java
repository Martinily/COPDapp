package com.funnyseals.app.feature.patientPersonalCenter;

public class Bean {
    private String equipment_title;
    private String equipment_name;
    private String equipment_state;

    public Bean(){}

    public Bean(String title,String name,String state){
        this.equipment_name=name;
        this.equipment_state=state;
        this.equipment_title=title;
    }

    public String getEquipment_title(){
        return equipment_title;
    }

    public String getEquipment_name(){
        return equipment_name;
    }

    public String getEquipment_state(){
        return equipment_state;
    }

    public void setEquipment_name(String equipment_name) {
        this.equipment_name = equipment_name;
    }

    public void setEquipment_state(String equipment_state) {
        this.equipment_state = equipment_state;
    }

    public void setEquipment_title(String equipment_title) {
        this.equipment_title = equipment_title;
    }
}
