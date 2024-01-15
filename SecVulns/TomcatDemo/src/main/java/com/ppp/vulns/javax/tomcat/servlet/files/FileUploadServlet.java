package com.ppp.vulns.javax.tomcat.servlet.files;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * @author Whoopsunix
 */
@MultipartConfig
@WebServlet("/file/upload/case3")
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        // 获取 ServletContext 对象
        ServletContext servletContext = getServletContext();

        // 获取 WEB-INF 目录下的资源路径
        String resourcePath = "/WEB-INF/";

        // 获取资源的真实路径
        String uploadPath = servletContext.getRealPath(resourcePath) + "upload/";
        System.out.println(uploadPath);
        // 检查是否为文件上传请求
        if (ServletFileUpload.isMultipartContent(request)) {
            // 创建文件上传处理工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();

            // 设置临时文件存储目录（可选）
//            File tempDir = new File("/tmp");
//            factory.setRepository(tempDir);

            // 创建文件上传处理器
            ServletFileUpload upload = new ServletFileUpload(factory);

            try {
                // 解析请求，获取文件项列表
                List<FileItem> items = upload.parseRequest(request);

                // 处理每个文件项
                for (FileItem item : items) {
                    // 检查是否为普通表单字段还是文件项
                    if (item.isFormField()) {
                        // 普通表单字段处理
                        String fieldName = item.getFieldName();
                        String fieldValue = item.getString("UTF-8");
                        System.out.println(fieldName + ": " + fieldValue);
                    } else {
                        // 文件项处理
                        String fieldName = item.getFieldName();
                        String fileName = new File(item.getName()).getName();
//                        String fileName = "xxx.txt";

                        // 可以保存文件到指定目录
                        File uploadedFile = new File(uploadPath + fileName);
                        item.write(uploadedFile);

                        System.out.println("File uploaded: " + fileName);
                    }
                }

                response.getWriter().println("File(s) uploaded successfully!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
        }
    }
}
