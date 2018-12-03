package com.funnyseals.app.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/12/01
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class UserDao {

    private static String URL      = "jdbc:mysql://10.63.60.135:3306/copd";
    private static String USERNAME = "copd";
    private static String PASSWORD= "123456";

    public static Connection getConnection() throws ClassNotFoundException {
        Connection conn = null;
        Class.forName("com.mysql.jdbc.Driver");
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
