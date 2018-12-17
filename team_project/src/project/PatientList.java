package project;

public interface PatientList {
    static void patientList(String docID)
    {
        String sql="select * from patient where docID='"+docID+"';";

        try
        {
            String result=DatabaseConnect.SqlExecuteQuery(sql);
            ReturnMessage.ReturnMessage(result,2022);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
