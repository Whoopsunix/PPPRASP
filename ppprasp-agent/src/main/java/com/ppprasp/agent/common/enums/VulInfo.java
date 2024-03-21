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
    FileUpload("File Upload"),
    FileRead("File Read"),
    FileDirectory("File Directory"),

    /**
     * MS
     */
    MS_Controller("Spring Controller MemShell"),
    MS_Executor("Executor MemShell"),
    MS_Listener("Listener MemShell"),
    ;

    private final String description;

    VulInfo(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
