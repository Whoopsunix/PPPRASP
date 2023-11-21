package com.ppprasp.agent.common;

import com.ppprasp.agent.hook.source.HttpBundle;

/**
 * @author Whoopsunix
 * 参考 https://www.jrasp.com/algorithm/thread/thread_inject.html 中给的线程增强逻辑
 */
public class RASPContext {
    public static InheritableThreadLocal<Context> RASP_ThreadLocal = new InheritableThreadLocal<Context>() {
//        @Override
//        protected Context initialValue() {
//            return null;
//        }
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
     * 用于识别是否是来自请求的调用
     */
    public static class Context {
        private long beginTimestamp = System.currentTimeMillis();
        // http service
        private HttpBundle httpBundle = null;
        // websocket onMessage 捕获
        private Object websocketObject = null;
        // dubbo received
        private Object dubboRequest = null;

        public Context() {
        }

        public Context(HttpBundle httpBundle, Object websocketObject, Object dubboRequest) {
            this.httpBundle = httpBundle;
            this.websocketObject = websocketObject;
            this.dubboRequest = dubboRequest;
        }

        public void setBeginTimestamp(long beginTimestamp) {
            this.beginTimestamp = beginTimestamp;
        }

        public void setHttpBundle(HttpBundle httpBundle) {
            this.httpBundle = httpBundle;
        }

        public void setWebsocketObject(Object websocketObject) {
            this.websocketObject = websocketObject;
        }

        public void setDubboRequest(Object dubboRequest) {
            this.dubboRequest = dubboRequest;
        }

        public long getBeginTimestamp() {
            return beginTimestamp;
        }

        public HttpBundle getHttpBundle() {
            return httpBundle;
        }

        public Object getWebsocketObject() {
            return websocketObject;
        }

        public Object getDubboRequest() {
            return dubboRequest;
        }
    }
}
