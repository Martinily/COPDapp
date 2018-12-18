package com.funnyseals.server;

import org.json.JSONObject;

import java.io.IOException;

public interface ConfigureDevice
{
    static void configureDevice(String json) throws IOException
    {
        int port=2019;
        JSONObject jsonObject=new JSONObject(json);
        JSONObject jsonResult=new JSONObject();
        String result=null;
        String device_type=jsonObject.getString("device_type");
        String pID=jsonObject.getString("pID");

        if(device_type.equals("add"))
        {
            String eName=jsonObject.getString("eName");
            String eState="0";

            String sql="insert into patientequipment(pID,eName,eState) values('"+pID+"','"+eName+"','"+eState+"')";
            int sqlresult=DatabaseConnect.SqlExecuteUpdate(sql);

            if(sqlresult==0)
            {
                jsonResult.put("divece_result","false");
                System.out.println("添加失败");
            }
            else
            {
                jsonResult.put("divece_result","true");
                System.out.println("添加成功");
            }
            result=jsonResult.toString();
        }

        else if(device_type.equals("get"))
        {
            String sql="select * from patientequipment where pID='"+pID+"';";
            result=DatabaseConnect.SqlExecuteQuery(sql);
        }

        else if(device_type.equals("update"))
        {
            String eState=jsonObject.getString("eState");
            String eName=jsonObject.getString("eName");
            String sql="UPDATE patientequipment SET eState='"+eState+"' WHERE pID='"+pID+"' and eName='"+eName+"'";
            int sqlresult=DatabaseConnect.SqlExecuteUpdate(sql);

            if(sqlresult==0)
            {
                jsonResult.put("divece_result","false");
                System.out.println("添加失败");
            }
            else
            {
                jsonResult.put("divece_result","true");
                System.out.println("添加成功");
            }
            result=jsonResult.toString();
        }
        ReturnMessage.ReturnMessage(result,port);
    }
}
