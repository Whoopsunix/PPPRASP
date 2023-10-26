package com.example.vulenv.controller;

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
public class DeserializeController {
    @RequestMapping("/deserialize")
    public void deserialize(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // 反序列化
            String base64 = request.getParameter("base64");
            System.out.println(base64);
            byte[] bytes = Base64.getDecoder().decode(base64);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object object = objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
