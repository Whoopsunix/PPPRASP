package com.ppprasp.agent.hook.source;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ModuleLifecycle;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.ppprasp.agent.common.RASPContext;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;

/**
 * @author Whoopsunix
 *
 * Dubbo 请求
 */
@MetaInfServices(Module.class)
@Information(id = "rasp-dubbo", author = "Whoopsunix", version = "1.0.0")
public class DubboHook implements Module, ModuleLifecycle {
    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    public void getServletAccess() {
        try {
            String className = "org.apache.dubbo.remoting.ChannelHandler";
            String methodName = "received";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(className)
                    .includeSubClasses()
                    .onBehavior(methodName)
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            // 只关心顶层调用
                            if (!advice.isProcessTop()) {
                                return;
                            }

                            Object request = advice.getParameterArray()[1];

                            RASPContext.Context context = new RASPContext.Context();
                            context.setDubboRequest(request);
                            RASPContext.set(context);

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
        getServletAccess();
    }
}
