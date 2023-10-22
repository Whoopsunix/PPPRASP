package com.ppprasp.agent.common;

import com.alibaba.jvm.sandbox.api.ProcessController;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author Whoopsunix
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
     * 修改响应信息
     *
     * @param response
     */
    public static void changeResponse(HttpServletResponse response) {
        try {
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
