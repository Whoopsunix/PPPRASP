package com.ppprasp.agent.hook;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ModuleLifecycle;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.ppprasp.agent.check.DeserializationChecker;
import com.ppprasp.agent.common.RASPConfig;
import com.ppprasp.agent.common.RASPContext;
import com.ppprasp.agent.common.RASPManager;
import com.ppprasp.agent.common.RASPVulType;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;
import java.io.ObjectStreamClass;

/**
 * @author Whoopsunix
 *
 * 反序列化
 */
@MetaInfServices(Module.class)
@Information(id = "rasp-deserialization-hook", author = "Whoopsunix", version = "1.1.0")
public class DeserializationHook implements Module, ModuleLifecycle {
    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    /**
     * java.io.ObjectInputStream.resolveClass()
     */
    public void checkResolveClass() {
        try {
            String className = "java.io.ObjectInputStream";
            String methodName = "resolveClass";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(Class.forName(className))
                    .includeBootstrap()
                    .includeSubClasses()
                    .onBehavior(methodName)
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            ObjectStreamClass objectStreamClass = (ObjectStreamClass) advice.getParameterArray()[0];
                            String classNameWithSerialVersionUID = objectStreamClass.toString();
                            String className = objectStreamClass.getName();

                            RASPContext.Context context = RASPContext.getContext();
                            if (DeserializationChecker.isDangerousClass(className) && context != null) {
                                String cve = RASPManager.showStackTracerWithCVECheck();
                                RASPManager.changeResponse(context.getHttpBundle());
                                String blockInfo;
                                if (cve != null) {
                                    blockInfo = String.format("[!] %s blocked by pppRASP, find black class %s triggered by %s [!]", RASPVulType.DESERIALIZATION, className, cve);
                                } else {
                                    blockInfo = String.format("[!] %s blocked by pppRASP, find black class %s [!]", RASPVulType.DESERIALIZATION, className);
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
        if (RASPConfig.isCheck("rasp-deserialization-hook", "resolveClass").equalsIgnoreCase("block")) {
            checkResolveClass();
        }
    }
}
