package com.ppprasp.agent.check;

/**
 * @author Whoopsunix
 */
public class ExpressionChecker {

    public static boolean isDangerousExpression(String expression) {
        for (String className: BlackClassInfo.dangerousBlackClassMap.keySet()){
            if (expression.contains(className)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDangerousClass(String className) {
        if (BlackClassInfo.dangerousBlackClassMap.containsKey(className)) {
            return true;
        }

        return false;
    }
}
