package com.ppprasp.agent.hook.vul;

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
import com.ppprasp.agent.common.enums.Algorithm;
import com.ppprasp.agent.common.enums.Status;
import com.ppprasp.agent.common.enums.VulInfo;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;

/**
 * @author Whoopsunix
 *
 * JNDI 注入
 */
@MetaInfServices(Module.class)
@Information(id = "rasp-jndi", author = "Whoopsunix", version = "1.0.0")
public class JNDIHook implements Module, ModuleLifecycle {

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    public void checkNaming() {
        Status status = RASPConfig.getAlgoStatus(Algorithm.JNDI.getAlgoId(), Algorithm.JNDI.getAlgoName());
        if (status == null || status == Status.CLOSE)
            return;

        try {
            // 接口
            String className = "javax.naming.Context";
            String methodName = "lookup";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(className)
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
                                    blockInfo = String.format("[!] %s blocked by pppRASP %s, triggered by %s [!]", VulInfo.JNDI.getDescription(), url, cve);
                                } else {
                                    blockInfo = String.format("[!] %s blocked by pppRASP %s [!]", url, VulInfo.JNDI.getDescription());
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
        checkNaming();
    }
}
