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
    private String mName;
    private String mSex;
    private int    mAge;
    private String mRegisterTime;
    private String mAddress;
    private String mCompany;
    private String mPosition;
    private String mPortrait;
    private String mMedicalHistory;
    private String mMedicalOrder;
    private String mMyDoctor;

    public User () {
    }

    public User (String account, String name, String sex, int age, String registerTime, String
            address, String medicalHistory, String medicalOrder, String myDoctor) {
        this.mAccount = account;
        this.mName = name;
        this.mSex = sex;
        this.mAge = age;
        this.mRegisterTime = registerTime;
        this.mAddress = address;
        this.mMedicalHistory = medicalHistory;
        this.mMedicalOrder = medicalOrder;
        this.mMyDoctor = myDoctor;
    }

    public User (String account, String name, String sex, int age, String registerTime, String
            address, String company, String position) {
        this.mAccount = account;
        this.mName = name;
        this.mSex = sex;
        this.mAge = age;
        this.mRegisterTime = registerTime;
        this.mAddress = address;
        this.mPosition = position;
        this.mCompany = company;
    }

    public String getAccount () {
        return mAccount;
    }

    public void setAccount (String account) {
        this.mAccount = account;
    }

    public String getName () {
        return mName;
    }

    public void setName (String nickname) {
        this.mName = nickname;
    }

    public String getSex () {
        return mSex;
    }

    public void setSex (String sex) {
        this.mSex = sex;
    }

    public int getAge () {
        return mAge;
    }

    public void setAge (int age) {
        this.mAge = age;
    }

    public String getRegisterTime () {
        return mRegisterTime;
    }

    public void setRegisterTime (String registerTime) {
        this.mRegisterTime = registerTime;
    }

    public String getAddress () {
        return mAddress;
    }

    public void setAddress (String address) {
        this.mAddress = address;
    }

    public String getCompany () {
        return mCompany;
    }

    public void setCompany (String company) {
        this.mCompany = company;
    }

    public String getPosition () {
        return mPosition;
    }

    public void setPosition (String position) {
        this.mPosition = position;
    }

    public String getPortrait () {
        return mPortrait;
    }

    public void setPortrait (String portrait) {
        this.mPortrait = portrait;
    }

    public String getMedicalHistory () {
        return mMedicalHistory;
    }

    public void setMedicalHistory (String medicalhistory) {
        this.mMedicalHistory = medicalhistory;
    }

    public String getMedicalOrder () {
        return mMedicalOrder;
    }

    public void setMedicalOrder (String medicalorder) {
        this.mMedicalOrder = medicalorder;
    }

    public String getMyDoctor () {
        return mMyDoctor;
    }

    public void setMyDoctor (String doctoraccount) {
        this.mMyDoctor = doctoraccount;
    }
}
