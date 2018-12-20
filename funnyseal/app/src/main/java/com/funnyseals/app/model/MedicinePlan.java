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
    private String mMedicineName;
    private String mMedicineDose;
    private String mMedicineNote;
    private String mMedicineTime;

    public MedicinePlan (String medicineName, String medicineDose, String medicineNote, String
            medicineTime) {

        this.mMedicineName = medicineName;
        this.mMedicineDose = medicineDose;
        this.mMedicineNote = medicineNote;
        this.mMedicineTime = medicineTime;
    }

    public String getMedicineName () {
        return mMedicineName;
    }

    public void setMedicineName (String medicineName) {
        this.mMedicineNote = medicineName;
    }

    public String getMedicineDose () {
        return mMedicineDose;
    }

    public void setMedicineDose (String medicineDose) {
        this.mMedicineDose = medicineDose;
    }

    public String getMedicineNote () {
        return mMedicineNote;
    }

    public void setMedicineNote (String medicineNote) {
        this.mMedicineNote = medicineNote;
    }

    public String getMedicineTime () {
        return mMedicineTime;
    }

    public void setMedicineTime (String medicineTime) {
        this.mMedicineTime = medicineTime;
    }
}
