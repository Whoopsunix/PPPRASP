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
import com.ppprasp.agent.common.enums.Middleware;
import com.ppprasp.agent.common.enums.Status;
import com.ppprasp.agent.common.enums.VulInfo;
import com.ppprasp.agent.utils.Reflections;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * @author Whoopsunix
 * <p>
 * Spring 内存马
 */
@MetaInfServices(Module.class)
@Information(id = "rasp-ms-jetty", author = "Whoopsunix", version = "1.0.0")
public class JettyMemShellHook implements Module, ModuleLifecycle {

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    public void checkListener() {
        Status status = RASPConfig.getAlgoStatus(Algorithm.MSJettyListener.getAlgoId(), Algorithm.MSJettyListener.getAlgoName());
        if (status == null || status == Status.CLOSE)
            return;
        try {
//            String className = "org.eclipse.jetty.server.handler.ContextHandler";
//            String methodName = "addEventListener";
            String className = "org.eclipse.jetty.server.handler.ContextHandler";
            String methodName = "setEventListeners";

            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(className)
                    .includeBootstrap()
                    .onBehavior(methodName)
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            Object[] eventListeners = (Object[]) advice.getParameterArray()[0];
                            Object javaObject = eventListeners[0];

                            RASPContext.Context context = RASPContext.getContext();
                            if (context != null && !ClassChecker.hasLocalClassFile(javaObject.getClass())) {
                                RASPManager.showStackTracer();
                                RASPManager.changeResponse(context.getHttpBundle());
                                String blockInfo = String.format("[!] %s %s Blocked by PPPRASP [!]", Middleware.Jetty.getDescription(), VulInfo.MSListener.getDescription());

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
        checkListener();
    }
}
