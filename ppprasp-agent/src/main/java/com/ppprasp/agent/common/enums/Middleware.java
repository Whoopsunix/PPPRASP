package com.ppprasp.agent.common.enums;

/**
 * @author Whoopsunix
 * <p>
 * 组件信息
 */
public enum Middleware {
    Tomcat("Tomcat"),
    Spring("Spring"),
    Jetty("Jetty"),
    ;

    private final String description;

    Middleware(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
