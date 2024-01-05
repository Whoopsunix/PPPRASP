package com.ppp.vulns.springboot2.controller.serialization;

import com.ppp.vulns.core.vulns.serialization.Original;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author Whoopsunix
 */
@Controller
@RequestMapping("/deserialization")
public class SerializationController {
    @RequestMapping("/case1")
    @ResponseBody
    protected void binary(@RequestParam("file") MultipartFile file, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        InputStream fileContent = file.getInputStream();
        byte[] bytes = new byte[fileContent.available()];
        fileContent.read(bytes);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        objectInputStream.readObject();
    }

    @RequestMapping("/case2")
    @ResponseBody
    public void case1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String b64 = request.getParameter("b64");
            System.out.println(b64);

            Object object = Original.deserializeBase64(b64);

            PrintWriter writer = response.getWriter();
            writer.println(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
