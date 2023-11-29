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
import com.ppprasp.agent.common.RASPVulType;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;

/**
 * @author Whoopsunix
 *
 * JNDI 注入
 */
@MetaInfServices(Module.class)
@Information(id = "rasp-jndi-hook", author = "Whoopsunix", version = "1.0.0")
public class JNDIHook implements Module, ModuleLifecycle {

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    public void checkNaming() {
        try {
            String className = "javax.naming.Context";
            String methodName = "lookup";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(Class.forName(className))
                    .includeBootstrap()
                    .includeSubClasses()
                    .onBehavior(methodName)
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            String url = (String) advice.getParameterArray()[0];

                            RASPContext.Context context = RASPContext.getContext();
                            if (context != null) {
                                String cve = RASPManager.showStackTracerWithCVECheck();
                                RASPManager.changeResponse(context.getHttpBundle());
                                String blockInfo;
                                if (cve != null) {
                                    blockInfo = String.format("[!] %s blocked by pppRASP %s, triggered by %s [!]", RASPVulType.JNDI, url, cve);
                                } else {
                                    blockInfo = String.format("[!] %s blocked by pppRASP %s [!]", url, RASPVulType.JNDI);
                                }

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
        if (RASPConfig.isCheck("rasp-jndi-hook", "naming").equalsIgnoreCase("block")) {
            checkNaming();
        }

    }
}
