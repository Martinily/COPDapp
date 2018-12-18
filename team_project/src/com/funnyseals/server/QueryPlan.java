package com.funnyseals.server;

import org.json.JSONArray;
import org.json.JSONObject;

public interface QueryPlan
{
    static void queryPlan(String getJson,String user_type)
    {
        if(user_type.equals("d"))
        {
            DoctorQueryPlan(getJson);
        }
        else if(user_type.equals("p"))
        {
            JSONObject json=new JSONObject(getJson);
            String query_state=json.getString("query_state");
            if(query_state.equals("now"))
            {
                PatientQueryNowPlan(getJson);
            }
            else if(query_state.equals("all"))
            {
                PatientQueryAllPlan(getJson);
            }
        }
    }

    static void DoctorQueryPlan(String Json)
    {
        JSONObject json=new JSONObject(Json);
        String ID=json.getString("ID");

        String Sql="select * from plan where docID='"+ID+"'";
        PlanQuery(Sql);
    }

    static void PatientQueryNowPlan(String Json)
    {
        JSONObject json=new JSONObject(Json);
        String ID=json.getString("ID");

        String Sql="select * from plan where pID='"+ID+"' and planUseS = 1";
        try
        {
            String result=null;
            result=DatabaseConnect.SqlExecuteQuery(Sql);
            if(result.equals("empty"))
            {
                ReturnMessage.ReturnMessage(result,2019);
            }
            else
            {
                JSONArray jsonArray=new JSONArray(result);
                JSONObject jsonObject=jsonArray.getJSONObject(0);
                String planID=jsonObject.getString("planID");
                QueryPlanItem.queryPlanItem(planID);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    static void PatientQueryAllPlan(String Json)
    {
        JSONObject json=new JSONObject(Json);
        String ID=json.getString("ID");

        String Sql="select * from plan where pID='"+ID+"' order by planTime desc";
        PlanQuery(Sql);
    }

    static void PlanQuery(String Sql)
    {
        int port=2019;
        try
        {
            String result=null;
            result=DatabaseConnect.SqlExecuteQuery(Sql);
            ReturnMessage.ReturnMessage(result,port);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
