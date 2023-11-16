package com.ppprasp.agent.common;

import com.alibaba.jvm.sandbox.api.ProcessController;
import com.ppprasp.agent.check.CVEChecker;
import com.ppprasp.agent.hook.source.HttpBundle;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author Whoopsunix
 * <p>
 * RASP 控制代码
 */
public class RASPManager {
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
            response.setStatus(500);
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<html><body><h1>Block by pppRASP</h1></body></html>");
        } catch (Exception e) {

        }
    }

    /**
     * 执行拦截
     *
     * @param blockInfo
     * @throws Throwable
     */
    public static void throwException(String blockInfo) throws Throwable {
        // 打印
        System.out.println(blockInfo);

        // 记录日志

        ProcessController.throwsImmediately(new Exception(blockInfo));
    }


}
