package com.funnyseals.server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public interface ReleasePlan
{
    static void releasePlan(String Json)
    {
        JSONObject json=new JSONObject(Json);

        try
        {
            String docID=json.getString("docID");
            String pID=json.getString("pID");

            String getJsonArray=GetJsonArray.GetMessage(2020);
            CreatePlanItem(getJsonArray,docID,pID);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    static void CreatePlanItem(String getJsonArray,String docID,String pID) throws IOException
    {
        int port=2019;
        JSONArray jsonArray=new JSONArray(getJsonArray);
        JSONObject jsonResult=new JSONObject();
        String planID;
        String returnJson=null;

        JSONObject jsonObject=new JSONObject();
        planID=CcreatePlan(docID,pID);

        int jsonnum=jsonArray.length();

        String[] sql=new String[jsonnum];
        int medicineNum=0;
        int apparatusNum=0;
        int sportsNum=0;

        String medicineName=null;
        String medicineDose=null;
        String medicineTime=null;
        String medicineAttention=null;

        String apparatusName=null;
        String apparatusTime=null;
        String apparatusAttention=null;

        String sportsType=null;
        String sportsTime=null;
        String sportsAttention=null;

        for(int i=0;i<jsonnum;i++)
        {
            jsonObject=jsonArray.getJSONObject(i);
            if(jsonObject.getString("item_type").equals("med"))
            {
                medicineName=jsonObject.getString("mName");
                medicineDose=jsonObject.getString("mDose");
                medicineTime=jsonObject.getString("mTime");
                medicineAttention=jsonObject.getString("mAttention");
                sql[i]="insert into planm(planID,mName,mDose,mTime,mAttention) values("+planID+",'"+medicineName+"','"+medicineDose+"','"+medicineTime+"','"+medicineAttention+"')";
                medicineNum++;
            }

            else if(jsonObject.getString("item_type").equals("app"))
            {
                jsonObject=jsonArray.getJSONObject(i);
                apparatusName=jsonObject.getString("appName");
                apparatusTime=jsonObject.getString("appTime");
                apparatusAttention=jsonObject.getString("appAttention");
                sql[i]="insert into planapp(planID,appName,appTime,appAttention) values("+planID+",'"+apparatusName+"','"+apparatusTime+"','"+apparatusAttention+"')";
                apparatusNum++;
            }

            else if(jsonObject.getString("item_type").equals("sports"))
            {
                System.out.println("------------");
                jsonObject=jsonArray.getJSONObject(i);
                sportsType=jsonObject.getString("sType");
                sportsTime=jsonObject.getString("sTime");
                sportsAttention=jsonObject.getString("sAttention");
                System.out.println(sportsType);
                sql[i]="insert into plans(planID,sType,sTime,sAttention) values("+planID+",'"+sportsType+"','"+sportsTime+"','"+sportsAttention+"')";
                sportsNum++;
            }
        }
        String[] sql1=new String[3];
        sql1[0]="UPDATE plan SET planMedicineN="+medicineNum+" WHERE planID='"+planID+"'";
        sql1[1]="UPDATE plan SET planApparatusN="+apparatusNum+" WHERE planID='"+planID+"'";
        sql1[2]="UPDATE plan SET planSportN="+sportsNum+" WHERE planID='"+planID+"'";

        int result1=DatabaseConnect.SqlExecuteBatch(sql);
        int result2=DatabaseConnect.SqlExecuteBatch(sql1);
        if(result1==jsonnum&&result2==3)
        {
            jsonResult.put("item_result","true");
            System.out.println("添加成功");
        }
        else
        {
            jsonResult.put("item_result","false");
            System.out.println("添加失败");
        }
        returnJson=jsonResult.toString();
        ReturnMessage.ReturnMessage(returnJson,port);
    }

    static String CcreatePlan(String docID,String pID)
    {
        String planID;
        if(pID.equals("无"))
        {
            pID="6";
        }
        String[] Sql = new String[2];
        System.out.println("start_plan");

        SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date=df.format(new Date());

        Sql[0]="insert into plan(docID,pID,planTime) values('"+docID+"','"+pID+"','"+date+"')";
        Sql[1]="select planID from plan where planTime='"+date+"' AND docID='"+docID+"'";

        int sqlresult=DatabaseConnect.SqlExecuteUpdate(Sql[0]);
        if(sqlresult==0)
        {
            return "false";
        }
        String result=DatabaseConnect.SqlExecuteQuery(Sql[1]);
        JSONArray jsonarray=new JSONArray(result);
        planID=jsonarray.getJSONObject(0).getString("planID");
        return planID;
    }
}
