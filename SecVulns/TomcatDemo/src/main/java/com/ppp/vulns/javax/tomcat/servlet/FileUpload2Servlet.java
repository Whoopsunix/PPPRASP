package com.ppp.vulns.javax.tomcat.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.util.List;

/**
 * @author Whoopsunix
 */
@MultipartConfig
@WebServlet("/file/upload/case4")
public class FileUpload2Servlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Part file = request.getPart("file");
            String fileName = file.getSubmittedFileName();
            System.out.println(fileName);

            // 获取 ServletContext 对象
            ServletContext servletContext = getServletContext();

            // 获取 WEB-INF 目录下的资源路径
            String resourcePath = "/WEB-INF/";

            // 获取资源的真实路径
            String uploadPath = servletContext.getRealPath(resourcePath) + "upload/";
            System.out.println(uploadPath);

            String filePath = uploadPath + fileName;
            file.write(filePath);

        } catch (Exception e) {

        }

    }
}
