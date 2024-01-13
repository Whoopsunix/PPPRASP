package com.ppprasp.agent.check.info;

import java.util.HashMap;

/**
 * @author Whoopsunix
 *
 * 黑名单统一维护
 */
public class BlackClassInfo {
    /**
     * 反序列化 sink 点
     */
    public static HashMap<String, Object> sinkBlackClassMap = new HashMap<String, Object>() {{
        put("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl", null);
        put("org.apache.commons.collections.functors.InvokerTransformer", null);
    }};

    /**
     * 基本危险类
     */
    public static HashMap<String, Object> dangerousBlackClassMap = new HashMap<String, Object>() {{
        put("java.lang.Runtime", null);
        put("java.lang.ProcessBuilder", null);
    }};


}
