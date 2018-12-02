package com.funnyseals.app.model;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/12/01
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class MedicinePlan {
    private String medicineName;
    private String medicineDose;
    private String medicineNote;
    private String medicineTime;

    public MedicinePlan(String medicineName, String medicineDose, String medicineNote, String medicineTime) {

        this.medicineName = medicineName;
        this.medicineDose = medicineDose;
        this.medicineNote = medicineNote;
        this.medicineTime = medicineTime;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineNote = medicineName;
    }

    public String getMedicineDose() {
        return medicineDose;
    }

    public void setMedicineDose(String medicineDose) {
        this.medicineDose = medicineDose;
    }

    public String getMedicineNote() {
        return medicineNote;
    }

    public void setMedicineNote(String medicineNote) {
        this.medicineNote = medicineNote;
    }

    public String getMedicineTime() {
        return medicineTime;
    }

    public void setMedicineTime(String medicineTime) {
        this.medicineTime = medicineTime;
    }
}
