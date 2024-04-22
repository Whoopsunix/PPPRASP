package com.ppprasp.agent.check;

import com.ppprasp.agent.check.info.BlackClassInfo;

/**
 * @author Whoopsunix
 *
 * 表达式检测
 */
public class ExpressionChecker {

    /**
     * 危险类检查 语句检查
     * @param expression
     * @return
     */
    public static boolean isDangerousClass(String expression) {
        for (String className: BlackClassInfo.dangerousBlackClassMap.keySet()){
            if (expression.equalsIgnoreCase(className)) {
                return true;
            }
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
