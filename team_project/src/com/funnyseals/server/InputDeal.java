package com.funnyseals.server;

import org.json.JSONObject;

import java.io.IOException;

public interface InputDeal {

    static void BasicDeal(String message) {
        JSONObject json = new JSONObject(message);
        String type;
        type = json.getString("request_type");

        switch (type) {
            case "1"://登录
                new Thread(() -> {
                    try {
                        String username = json.getString("user_name");
                        String password = json.getString("user_pw");
                        Login.login(username, password);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
                break;
            case "2"://注册
                new Thread(() -> {
                    try {
                        String register_type = json.getString("register_type");
                        Register.register(message, register_type);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                break;
            case "3"://发布护理计划
                new Thread(() -> {
                    try {
                        ReleasePlan.releasePlan(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                break;
            case "4"://查看护理计划
                new Thread(() -> {
                    try {
                        String user_type = json.getString("user_type");
                        QueryPlan.queryPlan(message, user_type);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                break;
            case "5"://查看护理计划具体项目
                new Thread(() -> {
                    try {
                        String planID = json.getString("planID");
                        QueryPlanItem.queryPlanItem(planID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                break;
            case "6"://查看个人信息
                new Thread(() -> {
                    try {
                        String user_type = json.getString("user_type");
                        String ID = json.getString("ID");
                        PersonalInfor.personalInfor(ID, user_type);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                break;
            case "7"://修改个人信息
                new Thread(() -> {
                    try {
                        String user_type = json.getString("user_type");
                        ModifyInfo.modifyInfo(message, user_type);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                break;
            case "8"://修改密码
                new Thread(() -> {
                    try {
                        String modify_type = json.getString("modify_type");
                        PasswordModify.passwordModify(message, modify_type);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                break;
            case "9"://查看/添加/修改 患者设备
                new Thread(() -> {
                    try {
                        ConfigureDevice.configureDevice(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                break;
            case "11"://查看/添加 患者数据
                new Thread(() -> {
                    try {
                        PatientData.patientData(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                break;
            case "12"://查看患者列表
                new Thread(() -> {
                    try {
                        String docID = json.getString("docID");
                        PatientList.patientList(docID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                break;
            case "13"://查看药物/器械库
                new Thread(() -> {
                    try {
                        String base_type = json.getString("base_type");
                        QueryBase.queryBase(base_type);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                break;
            case "14":// 修改护理计划
                new Thread(() -> {
                    try {
                        UpdatePlan.updatePlan(message);
                        UpdatePlan.updatePlan(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                break;
            case "15":// 查看/添加  患者病情历史
                new Thread(() -> {
                    try {
                        String pID = json.getString("pID");
                        String history_type = json.getString("history_type");
                        ConditionHistory.conditionHistory(pID, history_type);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                break;
        }
    }

    public static void main(String[] args) {

    }
}

