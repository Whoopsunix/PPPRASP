package com.ppp.vulns.springboot2.controller;

import com.ppp.vulns.core.vulns.JNDI;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Whoopsunix
 */
@Controller
@RequestMapping("/jndi")
public class JNDIController {
    @RequestMapping("/case1")
    @ResponseBody
    public Object case1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String url = request.getParameter("url");
            System.out.println(url);

            Object result = JNDI.lookup(url);
            System.out.println(result);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
