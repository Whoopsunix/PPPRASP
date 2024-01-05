package com.ppp.vulns.core.safe;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author Whoopsunix
 */
public class SPEL {

    public static void main(String[] args) {
        String runtimeEcho = "new java.util.Scanner(T(java.lang.Runtime).getRuntime().exec('ifconfig').getInputStream()).useDelimiter(\"\\\\A\").next()";
        Object obj = spelSimpleEvaluationContext(runtimeEcho);
        System.out.println(obj);
    }

    /**
     * SimpleEvaluationContext
     */
    public static Object spelSimpleEvaluationContext(String payload) {
        EvaluationContext evaluationContext = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        return new SpelExpressionParser().parseExpression(payload).getValue(evaluationContext);
    }

    public static Object spelSafe(String payload) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("payload", payload);
        Expression expression = new SpelExpressionParser().parseExpression("#payload");
        return expression.getValue(context);
    }
}
