package com.funnyseals.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public interface ConditionHistory
{
    static void conditionHistory(String pID,String history_type) throws IOException
    {
        if(history_type.equals("getall"))
        {
            String result=GetAllHistory(pID);
            ReturnMessage.ReturnMessage(result,2019);
        }
        if(history_type.equals("getnow"))
        {
            String result=GetNowHistory(pID);
            ReturnMessage.ReturnMessage(result,2019);
        }
        else if(history_type.equals("add"))
        {

        }
    }

    static String GetAllHistory(String pID)
    {
        String sql="select * from patienthistory where pID='"+pID+"' and HistoryState = 1";
        String result=DatabaseConnect.SqlExecuteQuery(sql);
        return result;
    }

    static String GetNowHistory(String pID)
    {
        String sql="select * from patienthistory where pID='"+pID+"' and HistoryState = 1";
        String result=DatabaseConnect.SqlExecuteQuery(sql);
        if(result.equals("empty"))
        { }
        else
        {
            JSONArray jsonArray=new JSONArray(result);
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            result=jsonObject.toString();
        }
        return result;
    }
}
