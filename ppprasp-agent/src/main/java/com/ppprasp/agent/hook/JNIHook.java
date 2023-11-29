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
 * JNI 注入
 */
@MetaInfServices(Module.class)
@Information(id = "rasp-jni-hook", author = "Whoopsunix", version = "1.0.0")
public class JNIHook implements Module, ModuleLifecycle {

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    public void checkStatement() {
        try {
            String className = "java.lang.ClassLoader";
            String methodName = "loadLibrary0";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(Class.forName(className))
                    .includeBootstrap()
                    .onBehavior(methodName)
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            String filePath = advice.getParameterArray()[1].toString();
                            // todo 针对路径进行更进一步的防护

                            RASPContext.Context context = RASPContext.getContext();
                            if (context != null) {
                                RASPManager.showStackTracer();
                                RASPManager.changeResponse(context.getHttpBundle());
                                String blockInfo = String.format("[!] %s blocked by pppRASP, file path %s [!]", RASPVulType.JNI, filePath);
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
         * java.lang.ClassLoader.loadLibrary0()
         */
        if (RASPConfig.isCheck("rasp-jni-hook", "jni").equalsIgnoreCase("block")) {
            checkStatement();
        }
    }
}
