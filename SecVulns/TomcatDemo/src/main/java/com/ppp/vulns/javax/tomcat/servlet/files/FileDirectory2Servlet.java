package com.ppp.vulns.javax.tomcat.servlet.files;

import com.ppp.vulns.core.vulns.files.FileDirectory;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author Whoopsunix
 * <p>
 * 路径遍历
 */
@MultipartConfig
@WebServlet("/file/directory/case2")
public class FileDirectory2Servlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String filePath = FileUtils.getResourcePath() + request.getParameter("filePath");
        System.out.println(filePath);

        String[] dirs = FileDirectory.list(filePath);
        System.out.println(Arrays.toString(dirs));
    }
}
