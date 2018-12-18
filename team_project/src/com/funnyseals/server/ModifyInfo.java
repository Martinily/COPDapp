package com.funnyseals.server;

import org.json.JSONObject;


public interface ModifyInfo    //修改个人信息
{
    static void modifyInfo(String getJson,String user_type)
    {
        if(user_type.equals("d"))
        {
            DoctorModifyInfo(getJson);
        }
        else if(user_type.equals("p"))
        {
            PatientModifyInfo(getJson);
        }
    }

    static void DoctorModifyInfo(String Json)
    {
        JSONObject json=new JSONObject(Json);
        String ID=json.getString("docID");
        String name=json.getString("docName");
        String sex=json.getString("docSex");
        String age=json.getString("docAge");
        String address=json.getString("docAddress");
        String company=json.getString("docCompany");
        String title=json.getString("docTitle");
        System.out.println(ID+"!!!");
        String Sql="UPDATE doctor SET docName='"+name+"',docSex='"+sex+"',docAge="+age+",docAddress='"+address+"',docCompany='"+company+"',docTitle='"+title+"' ";
        Sql+="WHERE docID='"+ID+"'";
        InfoUpdate(Sql);
    }

    static void PatientModifyInfo(String Json)
    {
        JSONObject json=new JSONObject(Json);
        String ID=json.getString("pID");
        String name=json.getString("pName");
        String sex=json.getString("pSex");
        String age=json.getString("pAge");
        String address=json.getString("pAddress");
        System.out.println(ID+"!!!");
        String Sql="UPDATE patient SET pName='"+name+"',pSex='"+sex+"',pAge="+age+",pAddress='"+address+"' ";
        Sql+="WHERE pID='"+ID+"'";
        InfoUpdate(Sql);
    }

    static void InfoUpdate(String Sql)
    {
        int port=2019;
        try
        {
            int message=DatabaseConnect.SqlExecuteUpdate(Sql);
            JSONObject returnjson=new JSONObject();
            if(message==0)
            {
                returnjson.put("update_result","失败");
            }
            else
            {
                returnjson.put("update_result","成功");
            }
            ReturnMessage.ReturnMessage(returnjson.toString(),port);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
