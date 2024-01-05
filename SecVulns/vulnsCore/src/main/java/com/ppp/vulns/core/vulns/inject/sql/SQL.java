package com.ppp.vulns.core.vulns.inject.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Whoopsunix
 */
public class SQL {
    static final String JDBCDRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DBURL = "jdbc:mysql://127.0.0.1:3306/SecVulns?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
    static final String USER = "root";
    static final String PASS = "123456";

    public static void main(String[] args) throws Exception {
        List<Object> result;
        boolean re;

        result = select(1, null, null);
        result = select(1, "xxx' union select * from users#", "123");
        System.out.println(result);

        re = insert("inject", "123456");
        System.out.println(re);

        re = update("inject", "xxxx");
        System.out.println(re);

        re = delete("inject", "xxxx");
        System.out.println(re);
    }

    public static List<Object> select(Integer id, String username, String password) throws Exception {
        Class.forName(JDBCDRIVER);
        Connection connection = DriverManager.getConnection(DBURL, USER, PASS);
        Statement statement = connection.createStatement();

        String sql = null;

//        if (id != null) {
//            sql = String.format("select * from users where `id`=%d;", id);
//        } else
        if (username != null && password != null) {
            sql = String.format("select * from users where `username`='%s' and `password`='%s';", username, password);
        } else {
            return null;
        }
        System.out.println(sql);
        ResultSet resultSet = statement.executeQuery(sql);
        List<Object> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(new Users(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password")));
        }

        statement.close();
        connection.close();
        return result;
    }

    public static boolean insert(String username, String password) throws Exception {
        Class.forName(JDBCDRIVER);
        Connection connection = DriverManager.getConnection(DBURL, USER, PASS);
        Statement statement = connection.createStatement();
        String sql = String.format("insert into users(id, username, password) values(%d, '%s', '%s');", new Random().nextInt(Integer.MAX_VALUE - 1), username, password);
        System.out.println(sql);
        statement.execute(sql);
        statement.close();
        connection.close();

        return true;
    }

    public static boolean update(String username, String password) throws Exception {
        Class.forName(JDBCDRIVER);
        Connection connection = DriverManager.getConnection(DBURL, USER, PASS);
        Statement statement = connection.createStatement();

        String sql = String.format("update users set password='%s' where username='%s';", password, username);
        System.out.println(sql);
        statement.execute(sql);
        statement.close();
        connection.close();

        return true;
    }

    public static boolean delete(String username, String password) throws Exception {
        Class.forName(JDBCDRIVER);
        Connection connection = DriverManager.getConnection(DBURL, USER, PASS);
        Statement statement = connection.createStatement();

        String sql = String.format("delete from users where username='%s' and password='%s';", username, password);
        System.out.println(sql);
        statement.execute(sql);
        statement.close();
        connection.close();

        return true;
    }
}
