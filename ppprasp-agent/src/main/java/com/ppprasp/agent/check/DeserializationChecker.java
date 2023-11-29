package com.ppprasp.agent.check;

import com.ppprasp.agent.check.info.BlackClassInfo;

/**
 * @author Whoopsunix
 *
 * 反序列化检测
 */
public class DeserializationChecker {
    public static boolean isDangerousClass(String className) {
        if (BlackClassInfo.sinkBlackClassMap.containsKey(className)) {
            return true;
        }

        return false;
    }
}
