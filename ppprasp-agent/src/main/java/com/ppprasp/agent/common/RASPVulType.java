package com.ppprasp.agent.common;

/**
 * @author Whoopsunix
 *
 * RASP 覆盖漏洞的描述信息统一处理
 */
public class RASPVulType {
    public static String SPEL = "SPEL Expression";
    public static String OGNL = "OGNL Expression";
    public static String RCE = "RCE";
    public static String JNI = "JNI file load";
    public static String SQL = "SQL Inject";
    public static String DESERIALIZE = "Deserialize";
}
