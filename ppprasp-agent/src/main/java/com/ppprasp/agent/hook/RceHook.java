package com.ppprasp.agent.hook;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ModuleLifecycle;
import com.alibaba.jvm.sandbox.api.ProcessController;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
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
                                RASPManager.block(context, className, methodName);

//                                HttpServletResponse response = context.getHttpBundle().getResponse();
//                                response.setStatus(500);
//                                response.setContentType("text/html; charset=UTF-8");
//                                PrintWriter out = response.getWriter();
//                                out.println("<html><body><h1>Block by pppRASP</h1></body></html>");
//
//
//
//                                ProcessController.throwsImmediately(new Exception(String.format("[!] Rce blocked by pppRASP, %s.%s [!]", className, methodName)));
                            }

                            super.before(advice);
                        }

                    });
        } catch (Exception e) {

        }
    }

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
                            ProcessController.throwsImmediately(new Exception(String.format("[!] Rce blocked by ppprasp, %s.%s [!]", className, methodName)));
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
        // native
        checkProcessImpl();
    }
}
