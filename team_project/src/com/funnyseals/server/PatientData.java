package com.funnyseals.server;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface PatientData {
    static void patientData(String json) throws IOException {
        int port = 2019;
        JSONObject jsonObject = new JSONObject(json);
        JSONObject jsonResult = new JSONObject();
        String result = null;
        String data_type = jsonObject.getString("data_type");
        String pID = jsonObject.getString("pID");

        if (data_type.equals("add")) {
            String FEV1 = jsonObject.getString("FEV1");
            String FVC = jsonObject.getString("FVC");
            String VC = jsonObject.getString("VC");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = df.format(new Date());

            String sql = "insert into patientdata(pID,FEV1,FVC,VC,UpdateTime) values('" + pID + "'," + FEV1 + "," + FVC + "," + VC + ",'" + date + "')";
            int sqlresult = DatabaseConnect.SqlExecuteUpdate(sql);

            if (sqlresult == 0) {
                jsonResult.put("data_result", "false");
                System.out.println("添加失败");
            } else {
                jsonResult.put("data_result", "true");
                System.out.println("添加成功");
            }
            result = jsonResult.toString();
        } else if (data_type.equals("getall")) {
            String sql = "select * from patientdata where pID='" + pID + "'order by UpdateTime desc;";
            result = DatabaseConnect.SqlExecuteQuery(sql);
            /*
            JSONArray jsonArray=new JSONArray(result);
            JSONArray sendArray=new JSONArray();
            for(int i=0;i<7;i++)
            {
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                sendArray.put(i,jsonObject1);
            }
            result=sendArray.toString();*/
        }
        /*
        else if(data_type.equals("getnow"))
        {
            String sql="select * from patientdata where pID='"+pID+"' order by UpdateTime desc;";
            result=DatabaseConnect.SqlExecuteQuery(sql);
            JSONArray jsonArray=new JSONArray(result);
            JSONObject jsonObject1=jsonArray.getJSONObject(0);
            result=jsonObject1.toString();
        }*/
        ReturnMessage.ReturnMessage(result, port);
    }
}
