package com.ppp.vulns.springboot2.controller.serialization;

import com.ppp.vulns.core.vulns.serialization.FastjsonDemo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Whoopsunix
 */
@Controller
@RequestMapping("/fastjson")
public class FastjsonController {
    @RequestMapping("/case1")
    @ResponseBody
    public void case1(@RequestBody String requestBody, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(requestBody);

        Object result = FastjsonDemo.parseObject(requestBody);

        PrintWriter writer = response.getWriter();
        writer.println(result);
    }
}
