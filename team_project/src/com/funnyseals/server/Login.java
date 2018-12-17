package com.funnyseals.server;

import java.io .IOException;
import org.json.JSONArray;
import org.json.JSONObject;

public interface Login
{
    static void login(String username,String password) throws IOException
    {
        int port=2019;
        String[] Sql = new String[2];
        String result=null;
        System.out.println("start_login");

        String passwordFromDatabase = null;
        String usertypeFromDatabase = null;
        Sql[0]="select * from doctor where docID='"+username+"';";
        System.out.println(Sql[0]);
        Sql[1]="select * from patient where pID='"+username+"';";
        System.out.println(Sql[1]);
        System.out.println("4");

        String returnJson=null;
        result=DatabaseConnect.SqlExecuteQuery(Sql[0]);
        JSONObject json=new JSONObject();
        if(result.equals("empty"))
        {
            result=DatabaseConnect.SqlExecuteQuery(Sql[1]);
            if(result.equals("empty"))
            {
                json.put("login_state","用户不存在");//false
                System.out.println("用户不存在");
            }
            else
            {
                JSONArray jsonarray=new JSONArray(result);
                json=jsonarray.getJSONObject(0);
                passwordFromDatabase=json.getString("pPassword");
                usertypeFromDatabase ="p";
                json.put("user_type",usertypeFromDatabase);

                if(passwordFromDatabase.equals(password))
                {
                    json.put("login_state","成功");//true
                    String history=ConditionHistory.GetNowHistory(username);
                    JSONObject jsonObject=new JSONObject();
                    if(history.equals("empty"))
                    {
                        json.put("HistoryCondition","null");
                        json.put("HistoryAdvice","null");
                    }
                    else
                    {
                        jsonObject=new JSONObject(history);
                        String condition=jsonObject.getString("HistoryCondition");
                        String advice=jsonObject.getString("HistoryAdvice");
                        json.put("HistoryCondition",condition);
                        json.put("HistoryAdvice",advice);
                    }
                    System.out.println("send_message");
                }
                else
                {
                    json.put("login_state","密码错误");//false
                    System.out.println("密码错误");
                }
            }
        }
        else
        {
            JSONArray jsonarray=new JSONArray(result);
            json=jsonarray.getJSONObject(0);
            passwordFromDatabase=json.getString("docPassword");
            usertypeFromDatabase ="d";
            json.put("user_type",usertypeFromDatabase);

            if(passwordFromDatabase.equals(password))
            {
                json.put("login_state","成功");//true
                System.out.println("send_message");
            }
            else
            {
                json.put("login_state","密码错误");//false
                System.out.println("密码错误");
            }
        }
        returnJson=json.toString();
        System.out.print(returnJson);
        ReturnMessage.ReturnMessage(returnJson,port);
    }

}
