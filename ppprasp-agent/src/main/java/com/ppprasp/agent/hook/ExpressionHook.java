package com.ppprasp.agent.hook;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ModuleLifecycle;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.ppprasp.agent.check.ExpressionChecker;
import com.ppprasp.agent.common.RASPConfig;
import com.ppprasp.agent.common.RASPContext;
import com.ppprasp.agent.common.RASPManager;
import com.ppprasp.agent.utils.Reflections;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author Whoopsunix
 */
@MetaInfServices(Module.class)
@Information(id = "rasp-expression-hook", author = "Whoopsunix", version = "1.0.0")
public class ExpressionHook implements Module, ModuleLifecycle {
    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    public void checkSPELExpression() {
        try {
            String className = "org.springframework.expression.spel.standard.SpelExpression";
            String methodName = "getValue";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(Class.forName(className))
                    .includeBootstrap()
                    .onBehavior(methodName)
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            Object object = advice.getTarget();
                            String expression = (String) Reflections.getFieldValue(object, "expression");
                            RASPContext.Context context = RASPContext.getContext();
                            if (ExpressionChecker.isDangerousExpression(expression) && context != null) {
                                RASPManager.showStackTracer();
                                RASPManager.changeResponse(context.getHttpBundle().getResponse());
                                String blockInfo = String.format("[!] %s blocked by pppRASP, find dangerous expression %s [!]", "SPEL Expression", expression);
                                RASPManager.throwException(blockInfo);
                            }
                            super.before(advice);
                        }

                    });
        } catch (Exception e) {

        }
    }

    public void checkSPELClass() {
        try {
            String className = "org.springframework.expression.spel.ast.MethodReference$MethodValueRef";
            String methodName = "getValue";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(Class.forName(className))
                    .includeBootstrap()
                    .onBehavior(methodName)
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            Object target = advice.getTarget();
                            Object value = Reflections.getFieldValue(target, "value");
                            // 类名
                            String clsName = value.getClass().getName();
                            // 参数
                            Object[] arguments = (Object[]) Reflections.getFieldValue(target, "arguments");
                            // 调用方法
                            Object this$0 = Reflections.getFieldValue(target, "this$0");
                            Object mtdName = Reflections.getFieldValue(this$0, "name");

                            System.out.println(clsName);
                            System.out.println(Arrays.toString(arguments));
                            System.out.println(mtdName);

                            RASPContext.Context context = RASPContext.getContext();
                            if (ExpressionChecker.isDangerousClass(clsName) && context != null) {
                                RASPManager.showStackTracer();
                                RASPManager.changeResponse(context.getHttpBundle().getResponse());
                                String blockInfo = String.format("[!] %s blocked by pppRASP, find black class %s [!]", "SPEL Expression", clsName);
                                RASPManager.throwException(blockInfo);
                            }

                            super.before(advice);
                        }

                    });
        } catch (Exception e) {

        }
    }



    @Override
    public void onLoad() throws Throwable {

    }

    @Override
    public void onUnload() throws Throwable {

    }

    @Override
    public void onActive() throws Throwable {

    }

    @Override
    public void onFrozen() throws Throwable {

    }

    @Override
    public void loadCompleted() {
        /**
         * todo 看看覆盖的全不
         */
        if (RASPConfig.isCheck("rasp-expression-hook", "spelClass").equalsIgnoreCase("block")) {
            checkSPELClass();
        }

        /**
         * 原始的表达式检测
         */
        if (RASPConfig.isCheck("rasp-expression-hook", "spelExpression").equalsIgnoreCase("block")) {
            checkSPELExpression();
        }

    }
}
