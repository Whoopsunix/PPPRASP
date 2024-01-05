package com.ppp.vulns.springboot2.controller.expression;

import com.ppp.vulns.core.vulns.expression.OGNL;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Whoopsunix
 * <p>
 * 请求参数获取示例
 */
@Controller
@RequestMapping("/ognl")
public class OGNLController {
    @RequestMapping("/case1")
    @ResponseBody
    public Object case1(@RequestBody String requestBody, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(requestBody);
        Object object = OGNL.ognlGetValue(requestBody);
        return object;
    }

    @RequestMapping("/case2")
    @ResponseBody
    public void case2(@RequestBody String requestBody, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(requestBody);
        OGNL.ognlSetValue(requestBody);
    }

    @RequestMapping("/case3")
    @ResponseBody
    public Object case3(@RequestBody String requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println(requestBody);
        Object object = OGNL.ognlGetValueIbatis(requestBody);
        return object;
    }

    @RequestMapping("/case4")
    public void case4(@RequestBody String requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println(requestBody);
        OGNL.ognlSetValueIbatis(requestBody);
    }

}
