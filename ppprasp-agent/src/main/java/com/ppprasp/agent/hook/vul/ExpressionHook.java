package com.ppprasp.agent.hook.vul;

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
import com.ppprasp.agent.common.enums.Algorithm;
import com.ppprasp.agent.common.enums.Status;
import com.ppprasp.agent.common.enums.VulInfo;
import com.ppprasp.agent.utils.Reflections;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author Whoopsunix
 * <p>
 * 表达式注入
 */
@MetaInfServices(Module.class)
@Information(id = "rasp-expression", author = "Whoopsunix", version = "1.0.1")
public class ExpressionHook implements Module, ModuleLifecycle {
    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    /**
     * 原始的 SPEL 表达式检测
     */
    public void checkSPELExpression() {
        Status status = RASPConfig.getAlgoStatus(Algorithm.SPELExpression.getAlgoId(), Algorithm.SPELExpression.getAlgoName());
        if (status == null || status == Status.CLOSE)
            return;

        try {
            String className = "org.springframework.expression.spel.standard.SpelExpression";
            String methodName = "getValue";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(className)
                    .includeBootstrap()
                    .onBehavior(methodName)
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            // 获取 hook 的对象
                            Object object = advice.getTarget();
                            String expression = (String) Reflections.getFieldValue(object, "expression");

                            RASPContext.Context context = RASPContext.getContext();
                            if (ExpressionChecker.isDangerousSPELExpression(expression) && context != null) {
                                String cve = RASPManager.showStackTracerWithCVECheck();
                                RASPManager.changeResponse(context.getHttpBundle());
                                String blockInfo;
                                if (cve != null) {
                                    blockInfo = String.format("[!] %s blocked by PPPRASP, find dangerous expression %s triggered by %s [!]", VulInfo.SPEL.getDescription(), expression, cve);
                                } else {
                                    blockInfo = String.format("[!] %s blocked by PPPRASP, find dangerous expression %s [!]", VulInfo.SPEL.getDescription(), expression);
                                }

                                RASPManager.scheduler(status, blockInfo);
                            }
                            super.before(advice);
                        }

                    });
        } catch (Exception e) {

        }
    }


    // todo 直接获取表达式解析的类，避免绕过，还需要进一步测试看看效果
    public void checkSPELClass() {
        Status status = RASPConfig.getAlgoStatus(Algorithm.SPELClass.getAlgoId(), Algorithm.SPELClass.getAlgoName());
        if (status == null || status == Status.CLOSE)
            return;
        try {
            String className = "org.springframework.expression.spel.ast.MethodReference$MethodValueRef";
            String methodName = "getValue";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(className)
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
                            if (ExpressionChecker.isDangerousSPELClass(clsName) && context != null) {
                                RASPManager.showStackTracer();
                                RASPManager.changeResponse(context.getHttpBundle());
                                String blockInfo = String.format("[!] %s blocked by PPPRASP, find black class %s [!]", VulInfo.SPEL.getDescription(), clsName);

                                RASPManager.scheduler(status, blockInfo);
                            }

                            super.before(advice);
                        }

                    });
        } catch (Exception e) {

        }
    }

    /**
     * 原始的 OGNL 表达式检测
     */
    public void checkOGNLExpression() {
        Status status = RASPConfig.getAlgoStatus(Algorithm.OGNLExpression.getAlgoId(), Algorithm.OGNLExpression.getAlgoName());
        if (status == null || status == Status.CLOSE)
            return;
        try {
            String className = "ognl.Ognl";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(className)
                    .includeBootstrap()
                    .onBehavior("getValue")
                    .onBehavior("setValue")
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            RASPContext.Context context = RASPContext.getContext();
                            String ognl = (String) advice.getParameterArray()[0];

                            if (ExpressionChecker.isDangerousOGNLExpression(ognl) && context != null) {
                                RASPManager.showStackTracer();
                                RASPManager.changeResponse(context.getHttpBundle());
                                String blockInfo = String.format("[!] %s blocked by PPPRASP, find dangerous expression %s [!]", VulInfo.OGNL.getDescription(), ognl);

                                RASPManager.scheduler(status, blockInfo);
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
        checkSPELClass();
        checkSPELExpression();
        checkOGNLExpression();
    }
}
