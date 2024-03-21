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
        Status status = RASPConfig.getAlgoStatus(Algorithm.MS_Tomcat_Executor.getAlgoId(), Algorithm.MS_Tomcat_Executor.getAlgoName());
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
                                String blockInfo = String.format("[!] %s blocked by PPPRASP [!]", VulInfo.MS_Executor.getDescription());

                                RASPManager.scheduler(status, blockInfo);
                            }
                            super.before(advice);
                        }

                    });
        } catch (Exception e) {

        }
    }

    public void checkListener() {
        Status status = RASPConfig.getAlgoStatus(Algorithm.MS_Tomcat_Listener.getAlgoId(), Algorithm.MS_Tomcat_Listener.getAlgoName());
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
                                String blockInfo = String.format("[!] %s blocked by PPPRASP [!]", VulInfo.MS_Listener.getDescription());

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
    }
}
