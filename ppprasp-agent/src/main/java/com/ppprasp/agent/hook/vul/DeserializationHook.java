package com.ppprasp.agent.hook.vul;

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
import com.ppprasp.agent.common.enums.Algorithm;
import com.ppprasp.agent.common.enums.Status;
import com.ppprasp.agent.common.enums.VulInfo;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;
import java.io.ObjectStreamClass;

/**
 * @author Whoopsunix
 *
 * 反序列化
 */
@MetaInfServices(Module.class)
@Information(id = "rasp-deserialization", author = "Whoopsunix", version = "1.1.0")
public class DeserializationHook implements Module, ModuleLifecycle {
    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    /**
     * java.io.ObjectInputStream.resolveClass()
     */
    public void checkResolveClass() {
        Status status = RASPConfig.getAlgoStatus(Algorithm.Deserialization.getAlgoId(), Algorithm.Deserialization.getAlgoName());
        if (status == null || status == Status.CLOSE)
            return;

        try {
            String className = "java.io.ObjectInputStream";
            String methodName = "resolveClass";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(className)
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
                                    blockInfo = String.format("[!] %s Blocked by PPPRASP, find black class %s triggered by %s [!]", VulInfo.DESERIALIZATION.getDescription(), className, cve);
                                } else {
                                    blockInfo = String.format("[!] %s Blocked by PPPRASP, find black class %s [!]", VulInfo.DESERIALIZATION.getDescription(), className);
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
        checkResolveClass();
    }
}
