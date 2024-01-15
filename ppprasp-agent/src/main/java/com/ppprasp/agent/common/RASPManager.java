package com.ppprasp.agent.common;

import com.alibaba.jvm.sandbox.api.ProcessController;
import com.ppprasp.agent.check.CVEChecker;
import com.ppprasp.agent.common.enums.Status;
import com.ppprasp.agent.hook.source.bundle.HttpBundle;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author Whoopsunix
 *
 * RASP 控制代码
 *  涉及：栈、JVM 操作、输出
 */
public class RASPManager {

    public static void scheduler(Status status, String blockInfo) throws Throwable {
        if (status == Status.LOG) {
            RASPManager.log(blockInfo);
        } else {
            RASPManager.throwException(blockInfo);
        }
    }


    /**
     * 执行拦截
     *
     * @param blockInfo
     * @throws Throwable
     */
    public static void throwException(String blockInfo) throws Throwable {
        log(blockInfo);

        ProcessController.throwsImmediately(new Exception(blockInfo));
    }

    /**
     * 记录
     * @param blockInfo
     * @throws Throwable
     */
    public static void log(String blockInfo) throws Throwable {
        // 打印
        System.out.println(blockInfo);

    }




    /**
     * 打印调用栈
     */
    public static void showStackTracer() {
        try {
            // 打印调用栈
            List<String> stackList = StackTracer.getStack();
            System.out.println("[*] Rce blocked by pppRASP, stack trace [*]");
            for (String stack : stackList) {
                System.out.println(stack);
            }
        } catch (Exception e) {

        }
    }

    /**
     * CVE 检测单独提出来
     *
     */
    public static String showStackTracerWithCVECheck() {
        String cve = null;
        try {
            // 打印调用栈
            List<String> stackList = StackTracer.getStack();
            System.out.println("[*] Rce blocked by pppRASP, stack trace [*]");
            for (String stack : stackList) {
                if (cve == null)
                    cve = CVEChecker.isCVE(stack);
                System.out.println(stack);
            }
        } catch (Exception e) {

        }
        return cve;

    }

    /**
     * 修改响应信息
     *
     */
    public static void changeResponse(HttpBundle httpBundle) {
        try {
            if (httpBundle == null)
                return;
            HttpServletResponse response = httpBundle.getResponse();
            response.setStatus(200);
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<html><body><h1>Block by PPPRASP</h1></body></html>");
        } catch (Exception e) {

        }
    }






}
