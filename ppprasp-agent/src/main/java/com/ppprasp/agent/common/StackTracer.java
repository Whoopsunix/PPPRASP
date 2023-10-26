package com.ppprasp.agent.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Whoopsunix
 *
 * 获取调用栈
 */
public class StackTracer {
    /**
     * 获取调用栈
     * @return
     */
    public static List<String> getStack() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        List<String> stackList = new ArrayList<>();
        int stackLength = stackTraceElements.length;
        // 控制栈深度，避免超长调用链
        if (stackLength > 100) {
            stackLength = 100;
        }
        for (int i = stackLength - 1; i >= 0; i--) {
            stackList.add(stackTraceElements[i].toString());
            // 到 hook 包后不再继续获取
            if (stackTraceElements[i].getClassName().startsWith("com.ppprasp.agent.hook.")) {
                break;
            }
        }

        return stackList;
    }
}
