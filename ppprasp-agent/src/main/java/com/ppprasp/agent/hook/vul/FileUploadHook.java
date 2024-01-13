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
import com.ppprasp.agent.utils.FileCopyUtils;
import com.ppprasp.agent.utils.Reflections;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;

/**
 * @author Whoopsunix
 * <p>
 * 文件上传
 */
@MetaInfServices(Module.class)
@Information(id = "rasp-fileUpload", author = "Whoopsunix", version = "1.1.0")
public class FileUploadHook implements Module, ModuleLifecycle {

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    public void checkFileItem() {
        Status status = RASPConfig.getAlgoStatus(Algorithm.File_Upload.getAlgoId(), Algorithm.File_Upload.getAlgoName());
        if (status == null || status == Status.CLOSE)
            return;

        try {
            String className = "org.apache.tomcat.util.http.fileupload.FileItem";
            String methodName = "write";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(className)
                    .includeBootstrap()
                    .includeSubClasses()
                    .onBehavior(methodName)
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            byte[] bytes = new byte[0];
                            Object fileName = null;
                            Object object = advice.getTarget();

                            fileName = Reflections.getFieldValue(object, "fileName");
                            try {
                                bytes = FileCopyUtils.copyToByteArray((File) advice.getParameterArray()[0]);
                            } catch (Exception e) {

                            }

                            RASPContext.Context context = RASPContext.getContext();
                            if (context != null) {
                                RASPManager.showStackTracer();
                                RASPManager.changeResponse(context.getHttpBundle());
                                String blockInfo = String.format("[!] %s blocked by pppRASP, file name: %s, file content %s [!]", VulInfo.FileUpload.getDescription(), fileName, Arrays.toString(bytes));

                                RASPManager.scheduler(status, blockInfo);
                            }

                            super.before(advice);
                        }

                    });
        } catch (Exception e) {

        }

        try {
            String className = "org.apache.commons.fileupload.FileItem";
            String methodName = "write";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(className)
                    .includeBootstrap()
                    .includeSubClasses()
                    .onBehavior(methodName)
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            byte[] bytes = new byte[0];
                            Object fileName = null;
                            Object object = advice.getTarget();

                            fileName = Reflections.getFieldValue(object, "fileName");
                            try {
                                bytes = FileCopyUtils.copyToByteArray((File) advice.getParameterArray()[0]);
                            } catch (Exception e) {

                            }

                            RASPContext.Context context = RASPContext.getContext();
                            if (context != null) {
                                RASPManager.showStackTracer();
                                RASPManager.changeResponse(context.getHttpBundle());
                                String blockInfo = String.format("[!] %s blocked by pppRASP, file name: %s, file content %s [!]", VulInfo.FileUpload.getDescription(), fileName, Arrays.toString(bytes));

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
        checkFileItem();

    }
}
