package com.ppprasp.agent.hook;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ModuleLifecycle;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.ppprasp.agent.common.RASPConfig;
import com.ppprasp.agent.common.RASPContext;
import com.ppprasp.agent.common.RASPManager;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;

/**
 * @author Whoopsunix
 */
@MetaInfServices(Module.class)
@Information(id = "rasp-rce-hook", author = "Whoopsunix", version = "1.0.0")
public class RceHook implements Module, ModuleLifecycle {

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    /**
     * java.lang.ProcessBuilder.start()
     */
    public void checkProcessBuilder() {
        try {
            String className = "java.lang.ProcessBuilder";
            String methodName = "start";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(Class.forName(className))
                    .includeBootstrap()
                    .onBehavior(methodName)
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            RASPContext.Context context = RASPContext.getContext();
                            if (context != null) {
                                RASPManager.showStackTracer();
                                RASPManager.changeResponse(context.getHttpBundle().getResponse());
                                String blockInfo = String.format("[!] %s blocked by pppRASP, %s.%s() [!]", "RCE", className, methodName);
                                RASPManager.throwException(blockInfo);
                            }
                            super.before(advice);
                        }

                    });
        } catch (Exception e) {

        }
    }

    /**
     * native java.lang.UNIXProcess.forkAndExec
     */
    public void checkProcessImpl() {
        try {
            String className = "java.lang.UNIXProcess";
            String methodName = "forkAndExec";

            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(Class.forName(className))
                    .includeBootstrap()
                    .onBehavior(methodName)
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            RASPContext.Context context = RASPContext.getContext();
                            if (context != null) {
                                RASPManager.showStackTracer();
                                RASPManager.changeResponse(context.getHttpBundle().getResponse());
                                String blockInfo = String.format("[!] %s blocked by pppRASP, %s.%s() [!]", "RCE", className, methodName);
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
         * java.lang.ProcessBuilder.start()
         */
        if (RASPConfig.isCheck("rasp-rce-hook", "normal").equalsIgnoreCase("block")) {
            checkProcessBuilder();
        }

        /**
         * native java.lang.UNIXProcess.forkAndExec
         */
        if (RASPConfig.isCheck("rasp-rce-hook", "native").equalsIgnoreCase("block")) {
            checkProcessImpl();
        }
    }
}
