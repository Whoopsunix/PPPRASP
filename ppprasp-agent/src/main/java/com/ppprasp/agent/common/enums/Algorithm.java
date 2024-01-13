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
    SPELClass("rasp-expression", "spel-class"),
    SPELExpression("rasp-expression", "spel-expression"),
    OGNLExpression("rasp-expression", "ognl-expression"),

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
     * 文件读取
     */
    File_READ("rasp-file-read", "read"),

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
