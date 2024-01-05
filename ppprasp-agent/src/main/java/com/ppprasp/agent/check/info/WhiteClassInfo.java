package com.ppprasp.agent.check.info;

import com.ppprasp.agent.common.enums.Algorithm;

import java.util.HashMap;

/**
 * @author Whoopsunix
 *
 * 白名单名单统一维护
 *
 * 对于一些框架自调用的情况需要排查，比如各种 JSON 框架的生成
 */
public class WhiteClassInfo {
    /**
     * 调用栈白名单
     */
    public static HashMap<String, Object> sinkBlackClassMap = new HashMap<String, Object>() {{
        // Tomcat jsp 页面首次访问时编译成 Java 类 FileInputStream
        put("org.apache.jasper.JspCompilationContext", Algorithm.FILE_READ);
    }};


}
