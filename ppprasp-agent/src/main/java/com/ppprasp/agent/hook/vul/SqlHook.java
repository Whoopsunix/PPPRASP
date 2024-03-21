package com.ppprasp.agent.hook.vul;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ModuleLifecycle;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.ppprasp.agent.check.SqlChecker;
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
 * SQL 注入
 */
@MetaInfServices(Module.class)
@Information(id = "rasp-sql", author = "Whoopsunix", version = "1.0.0")
public class SqlHook implements Module, ModuleLifecycle {

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    /**
     * mysql com.mysql.cj.jdbc.StatementImpl 查询
     */
    public void checkStatement() {
        Status status = RASPConfig.getAlgoStatus(Algorithm.SQL_MYSQL.getAlgoId(), Algorithm.SQL_MYSQL.getAlgoName());
        if (status == null || status == Status.CLOSE)
            return;
        try {
            String className = "com.mysql.cj.jdbc.StatementImpl";
            new EventWatchBuilder(moduleEventWatcher)
                    .onClass(className)
                    .includeBootstrap()
                    .onBehavior("execute")
                    .onBehavior("executeQuery")
                    .onBehavior("executeUpdate")
                    .onBehavior("addBatch") // 批量
                    .onWatch(new AdviceListener() {
                        @Override
                        protected void before(Advice advice) throws Throwable {
                            RASPContext.Context context = RASPContext.getContext();
                            String sql = (String) advice.getParameterArray()[0];
                            if (context != null && sql != null && SqlChecker.isDangerous(sql)) {
                                RASPManager.showStackTracer();
                                RASPManager.changeResponse(context.getHttpBundle());
                                String blockInfo = String.format("[!] %s blocked by PPPRASP, %s [!]", VulInfo.SQL.getDescription(), sql);

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
        checkStatement();
    }
}
