package com.funnyseals.server;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.funnyseals.server.httpclient.demo.EasemobIMUsers;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public interface PasswordModify {
    static void passwordModify(String getJson, String modify_type) throws IOException {
        if (modify_type.equals("forget")) {
            ForgetPassword(getJson);
        } else if (modify_type.equals("update")) {
            UpdatePassword(getJson);
        }
    }

    static void ForgetPassword(String Json) throws IOException {
        int port = 2019;
        JSONObject json = new JSONObject(Json);
        String ID = json.getString("ID");
        String password = json.getString("Password");

        String[] Sql = new String[4];
        String result;

        Sql[0] = "select * from doctor where docID='" + ID + "';";
        Sql[1] = "select * from patient where pID='" + ID + "';";
        Sql[2] = "UPDATE doctor SET docPassword='" + password + "' WHERE docID='" + ID + "'";
        Sql[3] = "UPDATE patient SET pPassword='" + password + "' WHERE pID='" + ID + "'";

        String returnJson;
        result = DatabaseConnect.SqlExecuteQuery(Sql[0]);
        JSONObject json1 = new JSONObject();
        if (Objects.equals(result, "empty")) {
            result = DatabaseConnect.SqlExecuteQuery(Sql[1]);
            if (Objects.equals(result, "empty")) {
                json1.put("password_result", "false");//false
                System.out.println("-----------------user_not_exist-------------------");
            } else {
                int res = DatabaseConnect.SqlExecuteUpdate(Sql[3]);
                if (res == 0) {
                    json1.put("password_result", "false");
                    System.out.println("-----------------modify_failed-------------------");
                } else {
                    json1.put("password_result", "true");
                    System.out.println("-----------------modify_succeed-------------------");
                }
            }
        } else {
            int res = DatabaseConnect.SqlExecuteUpdate(Sql[2]);
            if (res == 0) {
                json1.put("password_result", "false");
                System.out.println("-----------------modify_failed-------------------");
            } else {
                ObjectNode json2 = JsonNodeFactory.instance.objectNode();
                json2.put("newpassword", password);
                EasemobIMUsers.modifyIMUserPasswordWithAdminToken(ID, json2);
                json1.put("password_result", "true");
                System.out.println("-----------------modify_succeed-------------------");
            }
        }
        returnJson = json1.toString();
        System.out.print(returnJson);
        ReturnMessage.ReturnMessage(returnJson, port);
    }

    static void UpdatePassword(String Json) throws IOException {
        int port = 2019;
        JSONObject json = new JSONObject(Json);
        String ID = json.getString("ID");
        String oldPassword = json.getString("oldPassword");
        String newPassword = json.getString("newPassword");

        String[] Sql = new String[4];
        String result;

        Sql[0] = "select * from doctor where docID='" + ID + "';";
        Sql[1] = "select * from patient where pID='" + ID + "';";
        Sql[2] = "UPDATE doctor SET docPassword='" + newPassword + "' WHERE docID='" + ID + "'";
        Sql[3] = "UPDATE patient SET pPassword='" + newPassword + "' WHERE pID='" + ID + "'";

        String returnJson;
        result = DatabaseConnect.SqlExecuteQuery(Sql[0]);
        JSONObject json1 = new JSONObject();
        if (Objects.equals(result, "empty")) {
            result = DatabaseConnect.SqlExecuteQuery(Sql[1]);
            if (Objects.equals(result, "empty")) {
                json1.put("password_result", "false");//false
                System.out.println("-----------------user_not_exist-------------------");
            } else {
                int passwordResult = CheckPatientPassword(result, oldPassword);
                if (passwordResult == 0) {
                    json1.put("password_result", "false");
                    System.out.println("-----------------password_not_right-------------------");
                } else {
                    int res = DatabaseConnect.SqlExecuteUpdate(Sql[3]);
                    if (res == 0) {
                        json1.put("password_result", "false");
                        System.out.println("-----------------modify_failed-------------------");
                    } else {
                        json1.put("password_result", "true");
                        System.out.println("-----------------modify_succeed-------------------");
                    }
                }
            }
        } else {
            int passwordResult = CheckDoctorPassword(result, oldPassword);
            if (passwordResult == 0) {
                json1.put("password_result", "false");
                System.out.println("-----------------password_not_right-------------------");
            } else {
                int res = DatabaseConnect.SqlExecuteUpdate(Sql[3]);
                if (res == 0) {
                    json1.put("password_result", "false");
                    System.out.println("-----------------modify_failed-------------------");
                } else {
                    json1.put("password_result", "true");
                    System.out.println("-----------------modify_succeed-------------------");
                }
            }
        }
        returnJson = json1.toString();
        System.out.print(returnJson);
        ReturnMessage.ReturnMessage(returnJson, port);
    }

    static int CheckPatientPassword(String result, String oldPassword) {
        JSONArray jsonArray = new JSONArray(result);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        String password = jsonObject.getString("pPassword");
        if (password.equals(oldPassword)) {
            return 1;
        }
        return 0;
    }

    static int CheckDoctorPassword(String result, String oldPassword) {
        JSONArray jsonArray = new JSONArray(result);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        String password = jsonObject.getString("docPassword");
        if (password.equals(oldPassword)) {
            return 1;
        }
        return 0;
    }
}
