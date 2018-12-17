package project;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public interface PasswordModify
{
    static void passwordModify(String getJson,String Pmodify_type)throws IOException
    {
        if(Pmodify_type.equals("forget"))
        {
            ForgetPassword(getJson);
        }
        else if(Pmodify_type.equals("update"))
        {
            UpdatePassword(getJson);
        }
    }

    static void ForgetPassword(String Json)throws IOException
    {
        int port=2019;
        JSONObject json=new JSONObject(Json);
        String ID=json.getString("ID");
        String password=json.getString("Password");
        System.out.println(ID+"!!!");

        String[] Sql = new String[4];
        String result=null;

        Sql[0]="select * from doctor where docID='"+ID+"';";
        Sql[1]="select * from patient where pID='"+ID+"';";
        Sql[2]="UPDATE doctor SET docPassword='"+password+"' WHERE docID='"+ID+"'";
        Sql[3]="UPDATE patient SET pPassword='"+password+"' WHERE pID='"+ID+"'";

        String returnJson=null;
        result=DatabaseConnect.SqlExecuteQuery(Sql[0]);
        JSONObject json1=new JSONObject();
        if(result.equals("empty"))
        {
            result=DatabaseConnect.SqlExecuteQuery(Sql[1]);
            if(result.equals("empty"))
            {
                json1.put("password_result","false");//false
                System.out.println("用户不存在");
            }
            else
            {
                int res=DatabaseConnect.SqlExecuteUpdate(Sql[3]);
                if(res==0)
                {
                    json1.put("password_result","false");
                    System.out.println("修改失败");
                }
                else
                {
                    json1.put("password_result","true");
                }
            }
        }
        else
        {
            int res=DatabaseConnect.SqlExecuteUpdate(Sql[2]);
            if(res==0)
            {
                json1.put("password_result","false");
                System.out.println("修改失败");
            }
            else
            {
                json1.put("password_result","true");
            }
        }
        returnJson=json1.toString();
        System.out.print(returnJson);
        ReturnMessage.ReturnMessage(returnJson,port);
    }

    static void UpdatePassword(String Json) throws IOException
    {
        int port=2019;
        JSONObject json=new JSONObject(Json);
        String ID=json.getString("ID");
        String oldPassword=json.getString("oldPassword");
        String newPassword=json.getString("newPassword");
        System.out.println(ID+"!!!");

        String[] Sql = new String[4];
        String result=null;

        Sql[0]="select * from doctor where docID='"+ID+"';";
        Sql[1]="select * from patient where pID='"+ID+"';";
        Sql[2]="UPDATE doctor SET docPassword='"+newPassword+"' WHERE docID='"+ID+"'";
        Sql[3]="UPDATE patient SET pPassword='"+newPassword+"' WHERE pID='"+ID+"'";

        String returnJson=null;
        result=DatabaseConnect.SqlExecuteQuery(Sql[0]);
        JSONObject json1=new JSONObject();
        if(result.equals("empty"))
        {
            result=DatabaseConnect.SqlExecuteQuery(Sql[1]);
            if(result.equals("empty"))
            {
                json1.put("password_result","false");//false
                System.out.println("用户不存在");
            }
            else
            {
                int passwordResult=CheckPatientPassword(result,oldPassword);
                if(passwordResult==0)
                {
                    json1.put("password_result","false");
                    System.out.println("密码错误");
                }
                else
                {
                    int res=DatabaseConnect.SqlExecuteUpdate(Sql[3]);
                    if(res==0)
                    {
                        json1.put("password_result","false");
                        System.out.println("修改失败");
                    }
                    else
                    {
                        json1.put("password_result","true");
                        System.out.println("修改成功");
                    }
                }
            }
        }
        else
        {
            int passwordResult=CheckDoctorPassword(result,oldPassword);
            if(passwordResult==0)
            {
                json1.put("password_result","false");
                System.out.println("密码错误");
            }
            else
            {
                int res=DatabaseConnect.SqlExecuteUpdate(Sql[3]);
                if(res==0)
                {
                    json1.put("password_result","false");
                    System.out.println("修改失败");
                }
                else
                {
                    json1.put("password_result","true");
                    System.out.println("修改成功");
                }
            }
        }
        returnJson=json1.toString();
        System.out.print(returnJson);
        ReturnMessage.ReturnMessage(returnJson,port);
    }

    static int CheckPatientPassword(String result,String oldPassword)
    {
        JSONArray jsonArray=new JSONArray(result);
        JSONObject jsonObject=jsonArray.getJSONObject(0);
        String password=jsonObject.getString("pPassword");
        if(password.equals(oldPassword))
        {
            return 1;
        }
        return 0;
    }

    static int CheckDoctorPassword(String result,String oldPassword)
    {
        JSONArray jsonArray=new JSONArray(result);
        JSONObject jsonObject=jsonArray.getJSONObject(0);
        String password=jsonObject.getString("docPassword");
        if(password.equals(oldPassword))
        {
            return 1;
        }
        return 0;
    }
}
