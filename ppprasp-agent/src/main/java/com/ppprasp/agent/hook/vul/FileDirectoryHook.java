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
import com.ppprasp.agent.utils.Reflections;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;

/**
 * @author Whoopsunix
 * <p>
 * 文件漏洞 路径遍历
 */
@MetaInfServices(Module.class)
@Information(id = "rasp-file-directory", author = "Whoopsunix", version = "1.0.0")
public class FileDirectoryHook implements Module, ModuleLifecycle {

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    public void checkIOList() {
        Status status = RASPConfig.getAlgoStatus(Algorithm.File_Directory.getAlgoId(), Algorithm.File_Directory.getAlgoName());
        if (status == null || status == Status.CLOSE)
            return;

        ioList("java.io.File", "list", status);
        ioList("java.io.File", "normalizedList", status);
    }

    public void ioList(String className, String methodName, Status status) {
        try {
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(className)
                    .includeBootstrap()
                    .onBehavior(methodName)
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            RASPContext.Context context = RASPContext.getContext();
                            if (context != null) {
                                // 这个为常用方法 所以先判断是否为顶层调用  减少不必要的反射
                                Object file = advice.getTarget();
                                Object path = Reflections.getFieldValue(file, "path");
                                RASPManager.showStackTracer();
                                RASPManager.changeResponse(context.getHttpBundle());
                                String blockInfo = String.format("[!] %s blocked by PPPRASP,%s.%s() file path is %s [!]", VulInfo.FileDirectory.getDescription(), className, methodName, path);

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
        checkIOList();
    }
}
