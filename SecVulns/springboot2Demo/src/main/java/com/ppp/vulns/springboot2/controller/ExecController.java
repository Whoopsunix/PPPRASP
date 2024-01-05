package com.ppp.vulns.springboot2.controller;

import com.ppp.vulns.core.vulns.Exec;
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
@RequestMapping("/exec")
public class ExecController {
    @RequestMapping("/case1")
    @ResponseBody
    public Object case1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String str = request.getParameter("str");
            System.out.println(str);

            String result = Exec.runtime(str);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/case2")
    @ResponseBody
    public Object case2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String str = request.getParameter("str");
            System.out.println(str);

            String result = Exec.thread(str);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/case3")
    @ResponseBody
    public Object case3(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String str = request.getParameter("str");
            System.out.println(str);

            String result = Exec.processImpl(str);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/case4")
    @ResponseBody
    public Object case4(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String str = request.getParameter("str");
            System.out.println(str);

            String result = Exec.processBuilder(str);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/case5")
    @ResponseBody
    public Object case5(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String str = request.getParameter("str");
            System.out.println(str);

            String result = Exec.processImplUnixProcess(str);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/case6")
    @ResponseBody
    public Object case6(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String str = request.getParameter("str");
            System.out.println(str);

            String result = Exec.processImplUnixProcessByUnsafeNative(str);
            
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
