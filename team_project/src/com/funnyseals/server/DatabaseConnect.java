package com.funnyseals.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;

public interface DatabaseConnect {


    static Connection GetConnection() {
        Connection connection = null;
        String dbClassName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://140.143.70.205:3306/team?rewriteBatchedStatements=true&useSSL=true";
        String user = "root";
        String password = "WEIxin20131242";
        try {
            Class.forName(dbClassName).newInstance();
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (connection == null) {
            System.err.println("警告: DbConnectionManager.getConnection() 获得数据库链接失败.");
        }
        return connection;
    }

    static String SqlExecuteQuery(String Sql) {
        Connection connector;

        try {
            connector = GetConnection();

            Statement statement = connector.createStatement();

            ResultSet rs;
            rs = statement.executeQuery(Sql);//get data from database
            ResultSetMetaData rsmd = rs.getMetaData();
            int column = rsmd.getColumnCount();
            String val;
            String colName;
            JSONArray jsonarray = new JSONArray();
            if (!rs.next()) {
                return "empty";
            } else {
                rs.previous();
                int temp = 0;
                while (rs.next()) {
                    JSONObject json = new JSONObject();
                    for (int i = 1; i <= column; i++) {
                        colName = rsmd.getColumnLabel(i);
                        val = rs.getString(colName);
                        try {
                            json.put(colName, val);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    jsonarray.put(temp, json);
                    temp++;
                }
            }
            return jsonarray.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static int SqlExecuteUpdate(String Sql) {
        Connection connector;
        try {
            connector = GetConnection();
            System.out.println("-----------------connect_succeed-------------------");
            Statement statement = connector.createStatement();
            return statement.executeUpdate(Sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    static int SqlExecuteBatch(String[] Sql) {
        Connection connector;
        try {
            connector = GetConnection();
            System.out.println("-----------------connect_succeed-------------------");
            Statement statement = connector.createStatement();
            for (String s : Sql) {
                statement.addBatch(s);
            }
            int[] ReturnMessage = statement.executeBatch();//execute database
            int returnMessage = 0;
            for (int i1 : ReturnMessage) {
                if (i1 != 0)
                    returnMessage++;
            }
            return returnMessage;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


}