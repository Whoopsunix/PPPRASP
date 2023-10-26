package com.example.vulenv.controller;

import ognl.Ognl;
import ognl.OgnlContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Whoopsunix
 */
@Controller
@RequestMapping("/expression")
public class ExpressionController {
    @RequestMapping("/spel")
    public void spel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String spel = request.getParameter("spel");
            System.out.println(spel);
            new SpelExpressionParser().parseExpression(spel).getValue();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/ognlget")
    public void ognlGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String ognl = request.getParameter("ognl");
            Object obj = Ognl.getValue(ognl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/ognlset")
    public void ognlSet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String ognl = request.getParameter("ognl");
            Ognl.setValue(ognl, new OgnlContext(), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String spel = "T(java.lang.Runtime).getRuntime().exec('open -a Calculator.app')";
        System.out.println(spel);
        new SpelExpressionParser().parseExpression(spel).getValue();
    }
}
