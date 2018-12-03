package com.funnyseals.app.model;

import java.io.Serializable;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/11/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class User implements Serializable {

    private String mAccount;
    private String mNickName;
    private String mMedicalHistory;
    private String mMedicalOrder;
    private String mDoctorAccount;
    private int mAge;
    private String mSex;
    private String mIconUrl;

    public User() {
    }

    public User(String account, String name, String sex, int age) {
        this.mAccount = account;
        this.mNickName = name;
        this.mSex = sex;
        this.mAge = age;
    }

    public String getAccount() {
        return mAccount;
    }

    public void setAccount(String account) {
        this.mAccount = account;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int age) {
        this.mAge = age;
    }

    public String getNickName() {
        return mNickName;
    }

    public void setNickName(String nickname) {
        this.mNickName = nickname;
    }

    public String getSex() {
        return mSex;
    }

    public void setSex(String sex) {
        this.mSex = sex;
    }

    public String getMedicalHistory() {
        return mMedicalHistory;
    }

    public void setMedicalHistory(String medicalhistory) {
        this.mMedicalHistory = medicalhistory;
    }

    public String getMedicalOrder() {
        return mMedicalOrder;
    }

    public void setMedicalOrder(String medicalorder) {
        this.mMedicalOrder = medicalorder;
    }

    public String getDoctorAccount() {
        return mDoctorAccount;
    }

    public void setDoctorAccount(String doctoraccount) {
        this.mDoctorAccount = doctoraccount;
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    public void setIconUrl(String iconurl) {
        this.mIconUrl = iconurl;
    }

    @Override
    public String toString() {
        return "User{" +
                "Account=" + mAccount +
                ", age='" + mAge + '\'' +
                ", medicalhistory='" + mMedicalHistory + '\'' +
                ", medicalorder='" + mMedicalOrder + '\'' +
                ", doctoraccount='" + mDoctorAccount + '\'' +
                ", nickname='" + mNickName + '\'' +
                ", sex='" + mSex + '\'' +
                ", iconUrl='" + mIconUrl + '\'' +
                '}';
    }
}
