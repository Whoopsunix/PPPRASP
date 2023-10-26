package com.ppprasp.agent.check;

import java.util.HashMap;

/**
 * @author Whoopsunix
 */
public class DeserializeChecker {
    public static boolean isDangerousClass(String className) {
        if (BlackClassInfo.sinkBlackClassMap.containsKey(className)) {
            return true;
        }

        return false;
    }
}
