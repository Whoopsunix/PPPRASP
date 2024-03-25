package com.ppprasp.agent.hook.memshell;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ModuleLifecycle;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.ppprasp.agent.check.ClassChecker;
import com.ppprasp.agent.common.RASPConfig;
import com.ppprasp.agent.common.RASPContext;
import com.ppprasp.agent.common.RASPManager;
import com.ppprasp.agent.common.enums.Algorithm;
import com.ppprasp.agent.common.enums.Status;
import com.ppprasp.agent.common.enums.VulInfo;
import com.ppprasp.agent.utils.Reflections;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;

/**
 * @author Whoopsunix
 * <p>
 * Spring 内存马
 */
@MetaInfServices(Module.class)
@Information(id = "rasp-ms-tomcat", author = "Whoopsunix", version = "1.0.0")
public class TomcatMemShellHook implements Module, ModuleLifecycle {
    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    public void checkExecutor() {
        Status status = RASPConfig.getAlgoStatus(Algorithm.MSTomcatExecutor.getAlgoId(), Algorithm.MSTomcatExecutor.getAlgoName());
        if (status == null || status == Status.CLOSE)
            return;
        try {
            String className = "org.apache.tomcat.util.net.AbstractEndpoint";
            String methodName = "setExecutor";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(className)
                    .includeBootstrap()
                    .onBehavior(methodName)
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            Object executor = advice.getParameterArray()[0];

                            RASPContext.Context context = RASPContext.getContext();
                            if (context != null && !ClassChecker.hasLocalClassFile(executor.getClass())) {
                                RASPManager.showStackTracer();
                                RASPManager.changeResponse(context.getHttpBundle());
                                String blockInfo = String.format("[!] %s blocked by PPPRASP [!]", VulInfo.MSExecutor.getDescription());

                                RASPManager.scheduler(status, blockInfo);
                            }
                            super.before(advice);
                        }

                    });
        } catch (Exception e) {

        }
    }

    public void checkListener() {
        Status status = RASPConfig.getAlgoStatus(Algorithm.MSTomcatListener.getAlgoId(), Algorithm.MSTomcatListener.getAlgoName());
        if (status == null || status == Status.CLOSE)
            return;
        try {
            String className = "org.apache.catalina.core.StandardContext";
            String methodName = "addApplicationEventListener";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(className)
                    .includeBootstrap()
                    .onBehavior(methodName)
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            Object listener = advice.getParameterArray()[0];

                            RASPContext.Context context = RASPContext.getContext();
                            if (context != null && !ClassChecker.hasLocalClassFile(listener.getClass())) {
                                RASPManager.showStackTracer();
                                RASPManager.changeResponse(context.getHttpBundle());
                                String blockInfo = String.format("[!] %s blocked by PPPRASP [!]", VulInfo.MSListener.getDescription());

                                RASPManager.scheduler(status, blockInfo);
                            }
                            super.before(advice);
                        }

                    });
        } catch (Exception e) {

        }
    }

    public void checkServlet() {
        Status status = RASPConfig.getAlgoStatus(Algorithm.MSTomcatServlet.getAlgoId(), Algorithm.MSTomcatServlet.getAlgoName());
        if (status == null || status == Status.CLOSE)
            return;
        try {
            String className = "org.apache.catalina.core.ContainerBase";
            String methodName = "addChildInternal";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(className)
                    .includeBootstrap()
                    .onBehavior(methodName)
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            Object standardWrapper = advice.getParameterArray()[0];

                            Object instance = Reflections.getFieldValue(standardWrapper, "instance");
                            Object name = Reflections.getFieldValue(standardWrapper, "name");

                            RASPContext.Context context = RASPContext.getContext();
                            if (context != null && !ClassChecker.hasLocalClassFile(instance.getClass())) {
                                RASPManager.showStackTracer();
                                RASPManager.changeResponse(context.getHttpBundle());
                                String blockInfo = String.format("[!] %s blocked by PPPRASP, MemShell name is %s [!]", VulInfo.MSServlet.getDescription(), name);

                                RASPManager.scheduler(status, blockInfo);
                            }
                            super.before(advice);
                        }

                    });
        } catch (Exception e) {

        }
    }

    public void checkFilter() {
        Status status = RASPConfig.getAlgoStatus(Algorithm.MSTomcatFilter.getAlgoId(), Algorithm.MSTomcatFilter.getAlgoName());
        if (status == null || status == Status.CLOSE)
            return;
        try {
            String className = "org.apache.catalina.core.StandardContext";
            String methodName = "addFilterDef";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(className)
                    .includeBootstrap()
                    .onBehavior(methodName)
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            Object filterDef = advice.getParameterArray()[0];

                            Object filterName = Reflections.getFieldValue(filterDef, "filterName");
                            Object filterClass = Reflections.getFieldValue(filterDef, "filterClass");
                            Object filter = Reflections.getFieldValue(filterDef, "filter");

                            RASPContext.Context context = RASPContext.getContext();
                            if (context != null && !ClassChecker.hasLocalClassFile(filter.getClass())) {
                                RASPManager.showStackTracer();
                                RASPManager.changeResponse(context.getHttpBundle());
                                String blockInfo = String.format("[!] %s blocked by PPPRASP, MemShell name is %s, MemShell Class is %s [!]", VulInfo.MSFilter.getDescription(), filterName, filterClass);

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
        checkExecutor();
        checkListener();
        checkServlet();
        checkFilter();
    }
}
