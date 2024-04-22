package com.ppprasp.agent.common.enums;

/**
 * @author Whoopsunix
 *
 * 漏洞描述信息
 */
public enum VulInfo {
    DESERIALIZATION("Deserialization"),
    XMLDeserialization("XMLDeserialization"),
    SPEL("SPEL Expression"),
    OGNL("OGNL Expression"),
    JXpath("JXpath Expression"),
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
    MSController("Spring Controller MemShell"),
    MSExecutor("Executor MemShell"),
    MSListener("Listener MemShell"),
    MSServlet("Servlet MemShell"),
    MSFilter("Filter MemShell"),
    ;

    private final String description;

    VulInfo(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
