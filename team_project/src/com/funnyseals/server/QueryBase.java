package com.funnyseals.server;


public interface QueryBase
{
    static void queryBase(String base_type)
    {
        if(base_type.equals("med"))
        {
            QueryMedicineBase();
        }
        else if(base_type.equals("app"))
        {
            QueryApparatusBase();
        }
    }

    static void QueryMedicineBase()
    {
        String Sql="select * from medicine ";
        QueryBase(Sql,2020);
    }

    static void QueryApparatusBase()
    {
        String Sql="select * from apparatus ";
        QueryBase(Sql,2021);
    }

    static void QueryBase(String Sql,int port)
    {
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
