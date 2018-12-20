package com.funnyseals.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public interface UpdatePlan {
    static void updatePlan(String getJson) throws IOException {
        JSONObject json = new JSONObject(getJson);
        String update_type = json.getString("update_type");
        if (update_type.equals("docID")) {
            UpDateDocID(json);
        } else if (update_type.equals("pID")) {
            UpdatePID(json);
        } else if (update_type.equals("acceptState")) {
            UpdateAcceptState(json);
        } else if (update_type.equals("useState")) {
            UpdateUseState(json);
        } else if (update_type.equals("reUse")) {
            ReUsePlan(json);
        }
    }

    static void UpDateDocID(JSONObject json) throws IOException {
        String docID = json.getString("docID");
        String planID = json.getString("planID");

        String sql = "UPDATE plan SET docID='" + docID + "' WHERE planID='" + planID + "'";
        String result = SqlUpdate(sql);
        sendResult(result);
    }

    static void UpdatePID(JSONObject json) throws IOException {
        String pID = json.getString("docID");
        String planID = json.getString("planID");

        String sql = "UPDATE plan SET pID='" + pID + "' WHERE planID='" + planID + "'";
        String result = SqlUpdate(sql);
        sendResult(result);
    }

    static void UpdateAcceptState(JSONObject json) throws IOException {
        String acceptState = json.getString("planAcceptS");
        String planID = json.getString("planID");

        String sql = "UPDATE plan SET planAcceptS='" + acceptState + "' WHERE planID='" + planID + "'";
        String result = SqlUpdate(sql);
        sendResult(result);
    }

    static void UpdateUseState(JSONObject json) throws IOException {
        String useState = json.getString("planUseS");
        String planID = json.getString("planID");
        String oldPlanID = null;
        String pID = json.getString("pID");
        String[] sql = new String[3];

        sql[0] = "select * from plan where pID='" + pID + "' and planUseS=1";
        String result = SelectPlan(sql[0]);
        if (result.equals("empty")) {
        } else {
            oldPlanID = result;
            sql[1] = "update plan set planUseS = 0 where planID ='" + oldPlanID + "' ";
            int res = DatabaseConnect.SqlExecuteUpdate(sql[1]);
            if (res == 0) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("update_result", "false");
                result = jsonObject.toString();
                ReturnMessage.ReturnMessage(result, 2019);
            }
        }

        sql[2] = "UPDATE plan SET planUseS='" + useState + "',planAcceptS=1 WHERE planID='" + planID + "'";

        result = SqlUpdate(sql[2]);
        sendResult(result);
    }

    static void ReUsePlan(JSONObject json) throws IOException {
        String planID = json.getString("planID");
        String docID = json.getString("docID");
        String pID = json.getString("pID");
        String result = QueryPlanItem.queryItem(planID);
        ReleasePlan.CreatePlanItem(result, docID, pID);
    }

    static String SelectPlan(String sql) throws IOException {
        String jsonresult = DatabaseConnect.SqlExecuteQuery(sql);
        String result = "empty";
        if (jsonresult.equals("empty")) {
            return result;
        } else {
            JSONArray jsonArray = new JSONArray(jsonresult);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String planID = jsonObject.getString("planID");
            result = planID;
        }
        return result;
    }

    static String SqlUpdate(String sql) throws IOException {
        JSONObject jsonObject = new JSONObject();
        int sqlResult = DatabaseConnect.SqlExecuteUpdate(sql);
        if (sqlResult == 0) {
            jsonObject.put("update_result", "false");
        } else {
            jsonObject.put("update_result", "true");
        }
        String result = jsonObject.toString();
        return result;
    }

    static void sendResult(String result) throws IOException {
        ReturnMessage.ReturnMessage(result, 2019);
    }
}
