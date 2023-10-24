package com.example.vulenv.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Whoopsunix
 */
@Controller
@RequestMapping("/rce")
public class RceController {
    @RequestMapping("/thread")
    @ResponseBody
    public Object thread(HttpServletRequest request, HttpServletResponse response) {
        try {
            String cmd = request.getParameter("cmd");
            System.out.println(cmd);

            AtomicReference<InputStream> inputStreamRef = new AtomicReference<>();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream inputStream = Runtime.getRuntime().exec(cmd).getInputStream();
                        inputStreamRef.set(inputStream);
                    } catch (Exception e) {
                        // Handle the exception
                    }
                }
            });
            thread.start();
            thread.join();

            String result = new Scanner(inputStreamRef.get()).useDelimiter("\\A").next();
            System.out.println(result);
            return result;
        } catch (Exception e) {

        }
        return null;
    }

    @RequestMapping("/runtime")
    @ResponseBody
    public Object runtime(HttpServletRequest request, HttpServletResponse response) {
        try {
            String cmd = request.getParameter("cmd");
            System.out.println(cmd);
            InputStream inputStream = Runtime.getRuntime().exec(cmd).getInputStream();

            String result = new Scanner(inputStream).useDelimiter("\\A").next();
            System.out.println(result);
            return result;
        } catch (Exception e) {

        }
        return null;
    }
}
