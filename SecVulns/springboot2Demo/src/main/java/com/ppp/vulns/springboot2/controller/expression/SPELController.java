package com.ppp.vulns.springboot2.controller.expression;

import com.ppp.vulns.core.vulns.expression.SPEL;
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
@RequestMapping("/spel")
public class SPELController {
    @RequestMapping("/case1")
    @ResponseBody
    public Object case1(@RequestBody String requestBody, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(requestBody);
        Object object = SPEL.spel(requestBody);
        return object;
    }

    @RequestMapping("/case2")
    @ResponseBody
    public Object case2(@RequestBody String requestBody, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(requestBody);
        Object object = SPEL.spelStandardEvaluationContext(requestBody);
        return object;
    }

    @RequestMapping("/case3")
    @ResponseBody
    public Object case3(@RequestBody String requestBody, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(requestBody);
        Object object = SPEL.spelMethodBasedEvaluationContext(requestBody);
        return object;
    }

}
