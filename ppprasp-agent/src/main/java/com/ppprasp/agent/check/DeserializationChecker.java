package com.ppprasp.agent.check;

/**
 * @author Whoopsunix
 */
public class DeserializationChecker {
    public static boolean isDangerousClass(String className) {
        if (BlackClassInfo.sinkBlackClassMap.containsKey(className)) {
            return true;
        }

        return false;
    }
}
