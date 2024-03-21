package com.ppprasp.agent.check;

import java.io.File;
import java.net.URL;

/**
 * @author Whoopsunix
 */
public class ClassChecker {
    /**
     * 是否存在本地 Class 文件
     * @param clazz
     * @return
     */
    public static boolean hasLocalClassFile(Class<?> clazz) {
        String className = clazz.getName();
        String classFileName = className.replace('.', '/') + ".class";
        // 使用类加载器获取类文件
        URL resource = clazz.getClassLoader().getResource(classFileName);
        if (resource == null) {
            return false;
        } else {
            File classFile = new File(resource.getFile());
            return classFile.exists();
        }

//        // 尝试从文件系统路径中查找类文件
//        URL location = clazz.getProtectionDomain().getCodeSource().getLocation();
//        if (location == null) {
//            return false;
//        } else {
//            File classFile = new File(location.getPath() + classFileName);
//            return classFile.exists();
//        }
    }
}
