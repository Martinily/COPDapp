package com.funnyseals.app.model;

public class AddEquipment {
    private String equipment_name, equipment_state;

    public AddEquipment (String equipment_name, String equipment_state) {
        this.equipment_name = equipment_name;
        this.equipment_state = equipment_state;
    }

    public String getEquipment_name () {
        return equipment_name;
    }

    public String getEquipment_state () {
        return equipment_state;
    }
}
