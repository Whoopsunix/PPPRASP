package com.ppprasp.agent.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Whoopsunix
 *
 * 获取调用栈信息
 */
public class StackTracer {
    // 限制调用栈深度
    private static int maxStackLength = 100;

    /**
     * 获取调用栈为列表
     * @return
     */
    public static List<String> getStack() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        List<String> stackList = new ArrayList<>();
        int stackLength = stackTraceElements.length;
        // 控制栈深度，避免超长调用链
        if (stackLength > maxStackLength) {
            stackLength = maxStackLength;
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
