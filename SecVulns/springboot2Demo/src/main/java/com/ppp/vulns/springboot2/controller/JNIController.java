package com.ppp.vulns.springboot2.controller;

import com.ppp.vulns.core.vulns.JNI;
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
@RequestMapping("/jni")
public class JNIController {
    @RequestMapping("/case1")
    @ResponseBody
    public Object case1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String file = request.getParameter("file");
            System.out.println(file);
            JNI.load(file);

            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
