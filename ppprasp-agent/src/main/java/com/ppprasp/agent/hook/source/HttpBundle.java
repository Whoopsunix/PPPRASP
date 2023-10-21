package com.ppprasp.agent.hook.source;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Whoopsunix
 * 将 HTTP 请求中 request 和 response 封装为一个对象
 * 后续 HTTP 请求信息的完善都通过该对象
 */
public class HttpBundle {
    private HttpServletRequest request;
    private HttpServletResponse response;

    public HttpBundle(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }
}
