package project;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public interface PersonalInfor
{
    static void personalInfor(String ID,String user_type) throws IOException
    {
        int port=2023;
        if(user_type.equals("d"))
        {
            String result=DoctorInfo(ID);
            ReturnMessage.ReturnMessage(result,port);
        }
        else if(user_type.equals("p"))
        {
            String result=PatientInfo(ID);
            ReturnMessage.ReturnMessage(result,port);
        }
    }

    static String DoctorInfo(String docID)
    {
        String Sql="select * from doctor where docID='"+docID+"'";

        String result=InfoQuery(Sql);
        return result;
    }

    static String PatientInfo(String pID)
    {
        String Sql="select * from patient where pID='"+pID+"'";

        String result=InfoQuery(Sql);
        return result;
    }

    static String InfoQuery(String Sql)
    {
        try
        {
            String result=null;
            result=DatabaseConnect.SqlExecuteQuery(Sql);
            JSONArray jsonarray=new JSONArray(result);
            JSONObject json=jsonarray.getJSONObject(0);
            return json.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return "empty";
    }
}
