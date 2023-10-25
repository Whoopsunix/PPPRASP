package com.example.vulenv.controller;

import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Base64;

/**
 * @author Whoopsunix
 */
@Controller
@RequestMapping("/expression")
public class ExpressionController {
    @RequestMapping("/spel")
    public void thread(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String spel = request.getParameter("spel");
            System.out.println(spel);
            new SpelExpressionParser().parseExpression(spel).getValue();

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
