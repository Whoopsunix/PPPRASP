package com.ppprasp.agent.check;

/**
 * @author Whoopsunix
 */
public class ExpressionChecker {

    /**
     * SPEL 语句检查
     * @param expression
     * @return
     */
    public static boolean isDangerousSPELExpression(String expression) {
        for (String className: BlackClassInfo.dangerousBlackClassMap.keySet()){
            if (expression.contains(className)) {
                return true;
            }
        }
        return false;
    }

    /**
     * SPEL 类检查
     * @param className
     * @return
     */
    public static boolean isDangerousSPELClass(String className) {
        if (BlackClassInfo.dangerousBlackClassMap.containsKey(className)) {
            return true;
        }

        return false;
    }

    /**
     * OGNL 语句检查
     * @param expression
     * @return
     */
    public static boolean isDangerousOGNLExpression(String expression) {
        for (String className: BlackClassInfo.dangerousBlackClassMap.keySet()){
            if (expression.contains(className)) {
                return true;
            }
        }
        return false;
    }
}
