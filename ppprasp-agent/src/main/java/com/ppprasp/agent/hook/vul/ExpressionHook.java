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
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
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
     * 直接 hook 底层方法解析的接口
     */
    public void checkSPEL() {
        Status status = RASPConfig.getAlgoStatus(Algorithm.SPEL.getAlgoId(), Algorithm.SPEL.getAlgoName());
        if (status == null || status == Status.CLOSE)
            return;

        try {
            String hookClassName = "org.springframework.expression.MethodExecutor";
            String hookMethodName = "execute";

            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(hookClassName)
                    .includeBootstrap()
                    .includeSubClasses()
                    .onBehavior(hookMethodName)
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            // 获取 hook 的对象
                            // org.springframework.expression.spel.support.ReflectiveMethodExecutor.execute(EvaluationContext context, Object target, Object... arguments)
                            Object evaluationContext = advice.getParameterArray()[0];
                            Object target = advice.getParameterArray()[1];

                            String className;
                            if (target instanceof Class) {
                                className = ((Class<?>) target).getName();
                            } else {
                                className = target.getClass().getName();
                            }

                            RASPContext.Context context = RASPContext.getContext();
                            if (ExpressionChecker.isDangerousClass(className) && context != null) {
                                String cve = RASPManager.showStackTracerWithCVECheck();
                                RASPManager.changeResponse(context.getHttpBundle());
                                String blockInfo;
                                if (cve != null) {
                                    blockInfo = String.format("[!] %s Blocked by PPPRASP, find dangerous ClassName %s triggered by %s [!]", VulInfo.SPEL.getDescription(), className, cve);
                                } else {
                                    blockInfo = String.format("[!] %s Blocked by PPPRASP, find dangerous ClassName %s [!]", VulInfo.SPEL.getDescription(), className);
                                }

                                RASPManager.scheduler(status, blockInfo);
                            }
                            super.before(advice);
                        }

                    });
        } catch (Exception e) {

        }

        //        try {
//            String className = "org.springframework.expression.spel.standard.SpelExpression";
//            String methodName = "getValue";
//            new EventWatchBuilder(moduleEventWatcher)
//                    .onClass(className)
//                    .includeBootstrap()
//                    .onBehavior(methodName)
//                    .onWatch(new AdviceListener() {
//                        @Override
//                        protected void before(Advice advice) throws Throwable {
//                            // 获取 hook 的对象
//                            Object object = advice.getTarget();
//                            String expression = (String) Reflections.getFieldValue(object, "expression");
//
//                            RASPContext.Context context = RASPContext.getContext();
//                            if (ExpressionChecker.isDangerousSPELExpression(expression) && context != null) {
//                                String cve = RASPManager.showStackTracerWithCVECheck();
//                                RASPManager.changeResponse(context.getHttpBundle());
//                                String blockInfo;
//                                if (cve != null) {
//                                    blockInfo = String.format("[!] %s Blocked by PPPRASP, find dangerous expression %s triggered by %s [!]", VulInfo.SPEL.getDescription(), expression, cve);
//                                } else {
//                                    blockInfo = String.format("[!] %s Blocked by PPPRASP, find dangerous expression %s [!]", VulInfo.SPEL.getDescription(), expression);
//                                }
//
//                                RASPManager.scheduler(status, blockInfo);
//                            }
//                            super.before(advice);
//                        }
//
//                    });
//        } catch (Exception e) {
//
//        }
    }


//    public void checkSPELClass() {
//        Status status = RASPConfig.getAlgoStatus(Algorithm.SPELClass.getAlgoId(), Algorithm.SPELClass.getAlgoName());
//        if (status == null || status == Status.CLOSE)
//            return;
//        try {
//            String className = "org.springframework.expression.spel.ast.MethodReference$MethodValueRef";
//            String methodName = "getValue";
//            new EventWatchBuilder(moduleEventWatcher)
//                    .onClass(className)
//                    .includeBootstrap()
//                    .onBehavior(methodName)
//                    .onWatch(new AdviceListener() {
//                        @Override
//                        protected void before(Advice advice) throws Throwable {
//                            Object target = advice.getTarget();
//                            Object value = Reflections.getFieldValue(target, "value");
//                            // 类名
//                            String clsName = value.getClass().getName();
//                            // 参数
//                            Object[] arguments = (Object[]) Reflections.getFieldValue(target, "arguments");
//                            // 调用方法
//                            Object this$0 = Reflections.getFieldValue(target, "this$0");
//                            Object mtdName = Reflections.getFieldValue(this$0, "name");
//
//                            System.out.println(clsName);
//                            System.out.println(Arrays.toString(arguments));
//                            System.out.println(mtdName);
//
//                            RASPContext.Context context = RASPContext.getContext();
//                            if (ExpressionChecker.isDangerousSPELClass(clsName) && context != null) {
//                                RASPManager.showStackTracerWithCVECheck();
//                                RASPManager.changeResponse(context.getHttpBundle());
//                                String blockInfo = String.format("[!] %s Blocked by PPPRASP, find black class %s [!]", VulInfo.SPEL.getDescription(), clsName);
//
//                                RASPManager.scheduler(status, blockInfo);
//                            }
//
//                            super.before(advice);
//                        }
//
//                    });
//        } catch (Exception e) {
//
//        }
//    }

    /**
     * 原始的 OGNL 表达式检测
     */
    public void checkOGNL() {
        Status status = RASPConfig.getAlgoStatus(Algorithm.OGNL.getAlgoId(), Algorithm.OGNL.getAlgoName());
        if (status == null || status == Status.CLOSE)
            return;
        try {
            String hookClassName = "ognl.Ognl";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(hookClassName)
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
                                String blockInfo = String.format("[!] %s Blocked by PPPRASP, find dangerous expression %s [!]", VulInfo.OGNL.getDescription(), ognl);

                                RASPManager.scheduler(status, blockInfo);
                            }
                            super.before(advice);
                        }

                    });
        } catch (Exception e) {

        }
    }

    public void checkJXpath() {
        Status status = RASPConfig.getAlgoStatus(Algorithm.JXpath.getAlgoId(), Algorithm.JXpath.getAlgoName());
        if (status == null || status == Status.CLOSE)
            return;
        try {
            String hookClassName = "org.apache.commons.jxpath.Function";
            String hookMethodName = "invoke";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(hookClassName)
                    .includeBootstrap()
                    .includeSubClasses()
                    .onBehavior(hookMethodName)
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            Object target = advice.getTarget();

                            String className = null;
                            if (target.getClass().getName().equals("org.apache.commons.jxpath.functions.MethodFunction")) {
                                Method method = (Method) Reflections.getFieldValue(target, "method");
                                className = method.getDeclaringClass().getName();
                            } else if (target.getClass().getName().equals("org.apache.commons.jxpath.functions.ConstructorFunction")) {
                                Constructor constructor = (Constructor) Reflections.getFieldValue(target, "constructor");
                                className = constructor.getDeclaringClass().getName();
                            }

                            RASPContext.Context context = RASPContext.getContext();
                            if (ExpressionChecker.isDangerousClass(className) && context != null) {
                                String cve = RASPManager.showStackTracerWithCVECheck();
                                RASPManager.changeResponse(context.getHttpBundle());
                                String blockInfo;
                                if (cve != null) {
                                    blockInfo = String.format("[!] %s Blocked by PPPRASP, find dangerous ClassName %s triggered by %s [!]", VulInfo.JXpath.getDescription(), className, cve);
                                } else {
                                    blockInfo = String.format("[!] %s Blocked by PPPRASP, find dangerous ClassName %s [!]", VulInfo.JXpath.getDescription(), className);
                                }

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
//        checkSPELClass();
        checkSPEL();
        checkOGNL();
        checkJXpath();
    }
}
