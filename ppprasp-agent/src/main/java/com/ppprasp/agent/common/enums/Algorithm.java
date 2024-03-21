package com.ppprasp.agent.common.enums;

/**
 * @author Whoopsunix
 *
 * 算法名称
 */
public enum Algorithm {
    /**
     * 反序列化
     */
    Deserialization("rasp-deserialization", "resolveClass"),

    /**
     * 表达式
     */
    SPEL_Class("rasp-expression", "spel-class"),
    SPEL_Expression("rasp-expression", "spel-expression"),
    OGNL_Expression("rasp-expression", "ognl-expression"),

    /**
     * JNDI 注入
     */
    JNDI("rasp-jndi", "lookup"),

    /**
     * JNI 注入
     */
    JNI("rasp-jni", "loadLibrary"),

    /**
     * 命令执行
     */
    RCE_normal("rasp-rce", "normal"),
    RCE_native("rasp-rce", "native"),

    /**
     * SQL 注入
     */
    SQL_MYSQL("rasp-sql", "mysql"),

    /**
     * 文件上传
     */
    File_Upload("rasp-file-upload", "fileItem"),
    /**
     * 路径遍历
     */
    File_Directory("rasp-file-directory", "list"),
    /**
     * 文件读取
     */
    File_READ("rasp-file-read", "read"),

    /**
     * 内存马
     */
    MS_Spring("rasp-ms", "spring-controller")

    ;

    private final String algoId;
    private final String algoName;
    Algorithm(String algoId, String algoName) {
        this.algoId = algoId;
        this.algoName = algoName;
    }

    public String getAlgoId() {
        return algoId;
    }

    public String getAlgoName() {
        return algoName;
    }
}
