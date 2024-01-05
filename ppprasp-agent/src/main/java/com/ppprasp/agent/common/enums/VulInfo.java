package com.ppprasp.agent.common.enums;

/**
 * @author Whoopsunix
 *
 * 漏洞描述信息
 */
public enum VulInfo {
    DESERIALIZATION("Deserialization"),
    SPEL("SPEL Expression"),
    OGNL("OGNL Expression"),
    JNI("JNI file load"),
    JNDI("JNDI Injection"),
    RCE("RCE"),
    SQL("SQL Injection"),
    FILEREAD("File Read");

    private final String description;

    VulInfo(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
