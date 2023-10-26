package com.ppprasp.agent.check;

import java.util.HashMap;

/**
 * @author Whoopsunix
 *
 * CVE 检测
 */
public class CVEChecker {
    public static HashMap<String, String> cveStackTracer = new HashMap<String, String>() {{
        /**
         * SPEL
         */
        put("org.springframework.messaging.simp.broker.DefaultSubscriptionRegistry", "CVE-2018-1270, CVE-2018-1275");
    }};

    /**
     * 调用链是否包含 CVE 漏洞的触发类
     * @param className
     * @return
     */
    public static String isCVE(String className) {
        for (String cveClassName : cveStackTracer.keySet()) {
            if (className.startsWith(cveClassName)) {
                return cveStackTracer.get(cveClassName);
            }
        }
        return null;
    }

}
