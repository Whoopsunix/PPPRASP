package com.ppprasp.agent.check;

import java.util.HashMap;

/**
 * @author Whoopsunix
 */
public class DeserializeChecker {

    public static HashMap<String, Object> blackClassMap = new HashMap<String, Object>() {{
        put("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl", null);
    }};


    public static boolean isBlackClass(String className) {
        if (blackClassMap.containsKey(className)) {
            return true;
        }

        return false;
    }
}
