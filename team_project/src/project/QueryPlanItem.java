package project;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public interface QueryPlanItem
{

    static void queryPlanItem(String planID) throws IOException
    {
        int port=2019;
        String resultJson=queryItem(planID);
        ReturnMessage.ReturnMessage(resultJson,port);
    }


    static String queryItem(String planID) throws IOException
    {
        String Sql="select * from plan where planID='"+planID+"'";
        String result=DatabaseConnect.SqlExecuteQuery(Sql);
        JSONArray planArray=new JSONArray(result);
        JSONObject planJson=planArray.getJSONObject(0);
        String medNum=planJson.getString("planMedicineN");
        String appNum=planJson.getString("planApparatusN");
        String sportsNum=planJson.getString("planSportN");

        JSONArray MedJson=new JSONArray();
        JSONArray AppJson=new JSONArray();
        JSONArray SportJson=new JSONArray();
        JSONArray returnJsonArray=new JSONArray();
        if(Integer.valueOf(medNum)!=0)
        {
            MedJson=QueryMedicine(planID);
        }
        if(Integer.valueOf(appNum)!=0)
        {
            AppJson=QueryApparatus(planID);
        }
        if(Integer.valueOf(sportsNum)!=0)
        {
            SportJson=QuerySports(planID);
        }

        for(int i=0;i<MedJson.length();i++)
        {
            JSONObject jsonObject=new JSONObject();
            jsonObject=MedJson.getJSONObject(i);
            jsonObject.put("item_type","med");
            returnJsonArray.put(jsonObject);
        }
        for(int i=0;i<AppJson.length();i++)
        {
            JSONObject jsonObject=new JSONObject();
            jsonObject=AppJson.getJSONObject(i);
            jsonObject.put("item_type","app");
            returnJsonArray.put(jsonObject);
        }
        for(int i=0;i<SportJson.length();i++)
        {
            JSONObject jsonObject=new JSONObject();
            jsonObject=SportJson.getJSONObject(i);
            jsonObject.put("item_type","sports");
            returnJsonArray.put(jsonObject);
        }

        String resultJson=returnJsonArray.toString();
        return resultJson;
    }

    static JSONArray QueryMedicine(String planID) throws IOException
    {
        String Sql="select * from planm where planID='"+planID+"'";
        String result=ItemQuery(Sql);
        JSONArray jsonarray=new JSONArray(result);
        return jsonarray;
    }

    static JSONArray QueryApparatus(String planID) throws IOException
    {
        String Sql="select * from planapp where planID='"+planID+"'";
        String result=ItemQuery(Sql);
        JSONArray jsonarray=new JSONArray(result);
        return jsonarray;
    }

    static JSONArray QuerySports(String planID) throws IOException
    {
        String Sql="select * from plans where planID='"+planID+"'";
        String result=ItemQuery(Sql);
        JSONArray jsonarray=new JSONArray(result);
        return jsonarray;
    }

    static String ItemQuery(String sql)
    {
        String result=null;
        try
        {
            result=DatabaseConnect.SqlExecuteQuery(sql);
            return result;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        result="empty";
        return result;
    }
}
