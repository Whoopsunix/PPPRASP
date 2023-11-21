package com.ppprasp.agent.hook.source;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ModuleLifecycle;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.ppprasp.agent.common.RASPContext;
import com.ppprasp.agent.utils.InterfaceProxyUtils;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Whoopsunix
 */
@MetaInfServices(Module.class)
@Information(id = "rasp-http-hook", author = "Whoopsunix", version = "1.0.0")
public class HttpHook implements Module, ModuleLifecycle {
    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    protected HttpBundle wrapperHttpBundle(Advice advice) {
        // 俘虏 HttpServletRequest 参数为傀儡
        final HttpServletRequest request = InterfaceProxyUtils.puppet(HttpServletRequest.class, advice.getParameterArray()[0]);
        final HttpServletResponse response = InterfaceProxyUtils.puppet(HttpServletResponse.class, advice.getParameterArray()[1]);

        return new HttpBundle(request, response);
    }

    public void getServletAccess() {
        try {
            String className = "javax.servlet.http.HttpServlet";
            String methodName = "service";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(Class.forName(className))
                    .includeSubClasses()
                    .onBehavior(methodName)
                    .withParameterTypes("javax.servlet.http.HttpServletRequest", "javax.servlet.http.HttpServletResponse")
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            // 只关心顶层调用
                            if (!advice.isProcessTop()) {
                                return;
                            }

                            HttpBundle httpBundle = wrapperHttpBundle(advice);

                            RASPContext.Context context = new RASPContext.Context();
                            context.setHttpBundle(httpBundle);
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
