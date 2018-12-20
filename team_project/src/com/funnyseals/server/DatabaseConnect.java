package com.funnyseals.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public interface DatabaseConnect {


    static Connection GetConnection() {
        Connection connection = null;
        String dbClassName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://140.143.70.205:3306/team?rewriteBatchedStatements=true";
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

            ResultSet rs = null;
            rs = statement.executeQuery(Sql);//get data from database
            ResultSetMetaData rsmd = rs.getMetaData();
            int column = rsmd.getColumnCount();
            String val = null;
            String colName = null;
            JSONArray jsonarray = new JSONArray();
            System.out.println("TT");
            System.out.println(column);
            if (!rs.next()) {
                System.out.println("EE");
                return "empty";
            } else {
                System.out.println("11!!");
                rs.previous();
                System.out.println("11!!!!");
                int temp = 0;
                while (rs.next()) {
                    JSONObject json = new JSONObject();
                    for (int i = 1; i <= column; i++) {
                        colName = rsmd.getColumnLabel(i);
                        System.out.println("XX");
                        val = rs.getString(colName);
                        System.out.println("val" + val);
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

            if (!connector.isClosed())
                System.out.println("Connect succeeded!");
            Statement statement = connector.createStatement();

            System.out.println("Connect!");
            int returnMessage = statement.executeUpdate(Sql);//execute database
            System.out.println("Connect!!!!");
            return returnMessage;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    static int SqlExecuteBatch(String Sql[]) {
        Connection connector;
        try {
            connector = GetConnection();

            if (!connector.isClosed())
                System.out.println("Connect succeeded!");
            Statement statement = connector.createStatement();

            System.out.println("Connect!");
            for (int i = 0; i < Sql.length; i++) {
                statement.addBatch(Sql[i]);
            }
            int[] ReturnMessage = statement.executeBatch();//execute database
            int returnMessage = 0;
            for (int i = 0; i < ReturnMessage.length; i++) {
                if (ReturnMessage[i] != 0)
                    returnMessage++;
            }
            System.out.println("Connect!!!!");
            return returnMessage;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


}