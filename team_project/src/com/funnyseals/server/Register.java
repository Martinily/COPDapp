package com.funnyseals.server;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.funnyseals.server.httpclient.demo.EasemobIMUsers;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public interface Register {
    static void register(String getJson, String register_type) {
        if (register_type.equals("d")) {
            DoctorRegister(getJson);
        } else if (register_type.equals("p")) {
            PatientRegister(getJson);
        }
    }

    static void DoctorRegister(String Json) {
        JSONObject json = new JSONObject(Json);
        String ID = json.getString("ID");
        String password = json.getString("Password");
        String name = "医生";
        String sex = "男";
        String age = "30";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(new Date());
        String address = "请填写地址信息";
        String company = "请填写公司信息";
        String title = "请填写职称";
        String[] Sql = new String[2];
        Sql[0] = "select * from doctor where DocID='" + ID + "'";
        Sql[1] = "insert into doctor(docID,docPassword,docName,docSex,docAge,docTime,docAddress,docCompany,docTitle) ";
        Sql[1] += "values('" + ID + "','" + password + "','" + name + "','" + sex + "'," + age + ",'" + date + "','" + address + "','" + company + "','" + title + "')";

        RegisterCheck(Sql[0], Sql[1], json);
    }

    static void PatientRegister(String Json) {
        JSONObject json = new JSONObject(Json);
        String ID = json.getString("ID");
        String password = json.getString("Password");
        String name = "患者";
        String sex = "男";
        String age = "30";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(new Date());
        String address = "请填写地址";
        String[] Sql = new String[2];
        Sql[0] = "select * from patient where pID='" + ID + "'";
        Sql[1] = "insert into patient(docID,pID,pPassword,pName,pSex,pAge,pTime,pAddress) ";
        Sql[1] += "values('6','" + ID + "','" + password + "','" + name + "','" + sex + "'," + age + ",'" + date + "','" + address + "')";

        RegisterCheck(Sql[0], Sql[1], json);
    }

    static void RegisterCheck(String Sql1, String Sql2, JSONObject json) {
        int port = 2019;
        try {
            String result = null;
            result = DatabaseConnect.SqlExecuteQuery(Sql1);
            JSONObject returnjson = new JSONObject();
            if (Objects.equals(result, "empty")) {
                int ret = DatabaseConnect.SqlExecuteUpdate(Sql2);
                if (ret == 0) {
                    returnjson.put("reg_result", "失败");
                    System.out.println("-----------------register_failed-------------------");
                } else {
                    ObjectNode datanode = JsonNodeFactory.instance.objectNode();
                    datanode.put("username", json.getString("ID"));
                    datanode.put("password", json.getString("Password"));
                    EasemobIMUsers.createNewIMUserSingle(datanode);
                    returnjson.put("reg_result", "成功");
                    System.out.println("-----------------register_success-------------------");
                }
            } else {
                returnjson.put("reg_result", "用户已存在");
                System.out.println("-----------------user_has_been_registered-------------------");
            }
            ReturnMessage.ReturnMessage(returnjson.toString(), port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
