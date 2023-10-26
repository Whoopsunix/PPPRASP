package com.ppprasp.agent.hook;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ModuleLifecycle;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.ppprasp.agent.check.DeserializeChecker;
import com.ppprasp.agent.common.RASPConfig;
import com.ppprasp.agent.common.RASPContext;
import com.ppprasp.agent.common.RASPManager;
import com.ppprasp.agent.common.RASPVulType;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;
import java.io.ObjectStreamClass;

/**
 * @author Whoopsunix
 */
@MetaInfServices(Module.class)
@Information(id = "rasp-deserialize-hook", author = "Whoopsunix", version = "1.0.0")
public class DeserializeHook implements Module, ModuleLifecycle {
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
                    .onBehavior(methodName)
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            ObjectStreamClass objectStreamClass = (ObjectStreamClass) advice.getParameterArray()[0];
                            String classNameWithSerialVersionUID = objectStreamClass.toString();
                            String className = objectStreamClass.getName();

                            RASPContext.Context context = RASPContext.getContext();
                            if (DeserializeChecker.isDangerousClass(className) && context != null) {
                                RASPManager.showStackTracer();
                                RASPManager.changeResponse(context.getHttpBundle());
                                String blockInfo = String.format("[!] %s blocked by pppRASP, find black class %s [!]", RASPVulType.DESERIALIZE, className);
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
        if (RASPConfig.isCheck("rasp-deserialize-hook", "resolveClass").equalsIgnoreCase("block")) {
            checkResolveClass();
        }
    }
}
