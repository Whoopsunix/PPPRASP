package com.example.vulenv.controller;


import com.example.vulenv.common.DButil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Whoopsunix
 */
@Controller
@RequestMapping("/sql")
public class SQLController {
    @RequestMapping("/statement")
    @ResponseBody
    public Object statement(HttpServletRequest request, HttpServletResponse response){
        try {
            Connection c = DButil.getConnection();
            Statement s = c.createStatement();
            String sql = String.format("select * from `user` where `name`=\"%s\";", request.getParameter("name"));
            System.out.println(sql);
            ResultSet rs = s.executeQuery(sql);
            List<Object> results = new ArrayList<>();
            while (rs.next()) {
                results.add(new Object[]{rs.getInt("id"), rs.getString("name"), rs.getString("pass")});
            }

            return results;
        } catch (Exception e){

        }
        return null;
    }
}
