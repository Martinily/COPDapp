package com.funnyseals.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public interface Login {
    static void login(String username, String password) throws IOException {
        int port = 2019;
        String[] Sql = new String[2];
        String result;
        String passwordFromDatabase;
        Sql[0] = "select * from doctor where docID='" + username + "';";
        Sql[1] = "select * from patient where pID='" + username + "';";

        String returnJson;
        result = DatabaseConnect.SqlExecuteQuery(Sql[0]);
        JSONObject json = new JSONObject();
        System.out.println("------------------start_login---------------------");
        if (Objects.equals(result, "empty")) {
            result = DatabaseConnect.SqlExecuteQuery(Sql[1]);
            if (Objects.equals(result, "empty")) {
                json.put("login_state", "用户不存在");//false
                System.out.println("用户不存在");
            } else {
                JSONArray jsonarray = new JSONArray(result);
                json = jsonarray.getJSONObject(0);
                passwordFromDatabase = json.getString("pPassword");
                json.put("user_type", "p");

                if (passwordFromDatabase.equals(password)) {
                    json.put("login_state", "成功");//true
                    String history = ConditionHistory.GetNowHistory(username);
                    JSONObject jsonObject = new JSONObject();
                    if (history.equals("empty")) {
                        json.put("HistoryCondition", "null");
                        json.put("HistoryAdvice", "null");
                    } else {
                        jsonObject = new JSONObject(history);
                        String condition = jsonObject.getString("HistoryCondition");
                        String advice = jsonObject.getString("HistoryAdvice");
                        json.put("HistoryCondition", condition);
                        json.put("HistoryAdvice", advice);
                    }
                    System.out.println("-----------------send_login_message-------------------");
                } else {
                    json.put("login_state", "密码错误");//false
                    System.out.println("密码错误");
                }
            }
        } else {
            JSONArray jsonarray = new JSONArray(result);
            json = jsonarray.getJSONObject(0);
            passwordFromDatabase = json.getString("docPassword");
            json.put("user_type", "d");

            if (passwordFromDatabase.equals(password)) {
                json.put("login_state", "成功");//true
                System.out.println("-----------------send_login_message-------------------");
            } else {
                json.put("login_state", "密码错误");//false
                System.out.println("密码错误");
            }
        }
        returnJson = json.toString();
        System.out.print(returnJson);
        ReturnMessage.ReturnMessage(returnJson, port);
        System.out.println("-----------------login_succeed-------------------");
    }

}
