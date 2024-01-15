package com.ppp.vulns.javax.tomcat.servlet.files;

import com.ppp.vulns.core.vulns.files.FileDirectory;
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
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Whoopsunix
 * <p>
 * 路径遍历
 */
@MultipartConfig
@WebServlet("/file/directory/case1")
public class FileDirectoryServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String filePath = FileUtils.getResourcePath() + request.getParameter("filePath");
        System.out.println(filePath);

        String[] dirs = FileDirectory.listFiles(filePath);
        System.out.println(Arrays.toString(dirs));
    }
}
