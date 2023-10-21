package com.ppprasp.agent.common;

import com.ppprasp.agent.hook.source.HttpBundle;

/**
 * @author Whoopsunix
 * 参考 https://www.jrasp.com/algorithm/thread/thread_inject.html 中给的线程增强逻辑
 */
public class RASPContext {
    public static InheritableThreadLocal<Context> RASP_ThreadLocal = new InheritableThreadLocal<Context>() {
        @Override
        protected Context initialValue() {
            return null;
        }
    };

    public static Context getContext() {
        return RASP_ThreadLocal.get();
    }

    public static void set(Context context) {
        RASP_ThreadLocal.set(context);
    }

    public static void remove() {
        RASP_ThreadLocal.remove();
    }

    /**
     * 请求对象
     */
    public static class Context {
        private final long beginTimestamp = System.currentTimeMillis();
        private final HttpBundle httpBundle;

        public Context(HttpBundle httpBundle) {
            this.httpBundle = httpBundle;
        }

        public long getBeginTimestamp() {
            return beginTimestamp;
        }

        public HttpBundle getHttpBundle() {
            return httpBundle;
        }
    }
}
