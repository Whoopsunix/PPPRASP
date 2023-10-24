package com.example.vulenv.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Whoopsunix
 */

public class DButil {
    static String ip = "127.0.0.1";
    static int port = 3306;
    static String database = "test";
    static String encoding = "UTF-8";
    static String loginName = "root";
    static String password = "123456";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = String.format("jdbc:mysql://%s:%d/%s?characterEncoding=%s&serverTimezone=UTC", ip, port,
                database, encoding);
        //String url = String.format("jdbc:mysql://%s:%d?characterEncoding=%s&serverTimezone=UTC", ip, port,
        //      encoding);
        return DriverManager.getConnection(url, loginName, password);
    }

    public static void main(String[] args) throws SQLException {
        System.out.println(getConnection());

    }
}
