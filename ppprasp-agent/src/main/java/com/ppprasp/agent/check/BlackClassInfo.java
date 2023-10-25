package com.ppprasp.agent.check;

import java.util.HashMap;

/**
 * @author Whoopsunix
 *  黑名单统一维护类
 */
public class BlackClassInfo {
    /**
     * 反序列化 sink 点
     */
    public static HashMap<String, Object> sinkBlackClassMap = new HashMap<String, Object>() {{
        put("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl", null);
    }};

    /**
     * 基本危险类
     */
    public static HashMap<String, Object> dangerousBlackClassMap = new HashMap<String, Object>() {{
        put("java.lang.Runtime", null);
    }};


}
