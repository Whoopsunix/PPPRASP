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
 * 命令执行
 */
@MetaInfServices(Module.class)
@Information(id = "rasp-rce", author = "Whoopsunix", version = "1.1.0")
public class RceHook implements Module, ModuleLifecycle {

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    /**
     * java.lang.ProcessBuilder.start()
     */
    public void checkProcessBuilder() {
        Status status = RASPConfig.getAlgoStatus(Algorithm.RCENormal.getAlgoId(), Algorithm.RCENormal.getAlgoName());
        if (status == null || status == Status.CLOSE)
            return;
        try {
            String className = "java.lang.ProcessBuilder";
            String methodName = "start";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(className)
                    .includeBootstrap()
                    .onBehavior(methodName)
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            RASPContext.Context context = RASPContext.getContext();
                            if (context != null) {
                                String cve = RASPManager.showStackTracerWithCVECheck();
                                RASPManager.changeResponse(context.getHttpBundle());
                                String blockInfo;
                                if (cve != null) {
                                    blockInfo = String.format("[!] %s Blocked by PPPRASP, %s.%s() triggered by %s [!]", VulInfo.RCE.getDescription(), className, methodName, cve);
                                } else {
                                    blockInfo = String.format("[!] %s Blocked by PPPRASP, %s.%s() [!]", VulInfo.RCE.getDescription(), className, methodName);
                                }

                                RASPManager.scheduler(status, blockInfo);
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
    public void checkNative() {
        Status status = RASPConfig.getAlgoStatus(Algorithm.RCENative.getAlgoId(), Algorithm.RCENative.getAlgoName());
        if (status == null || status == Status.CLOSE)
            return;
        try {
            String className = "java.lang.UNIXProcess";
            String methodName = "forkAndExec";

            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(className)
                    .includeBootstrap()
                    .onBehavior(methodName)
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            RASPContext.Context context = RASPContext.getContext();
                            if (context != null) {
                                String cve = RASPManager.showStackTracerWithCVECheck();
                                RASPManager.changeResponse(context.getHttpBundle());
                                String blockInfo;
                                if (cve != null) {
                                    blockInfo = String.format("[!] %s Blocked by PPPRASP, %s.%s() triggered by %s [!]", VulInfo.RCE.getDescription(), className, methodName, cve);
                                } else {
                                    blockInfo = String.format("[!] %s Blocked by PPPRASP, %s.%s() [!]", VulInfo.RCE.getDescription(), className, methodName);
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
        checkProcessBuilder();
        checkNative();
    }
}
