package com.ppp.vulns.springboot2.controller.inject;

import com.ppp.vulns.core.vulns.inject.sql.SQL;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Whoopsunix
 */
@Controller
@RequestMapping("/sql/mysql")
public class SQLMysqlController {
    @RequestMapping("/case1")
    @ResponseBody
    public Object case1(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println(username);
        System.out.println(password);

        Object result = SQL.select(null, username, password);
        return result;
    }
}
