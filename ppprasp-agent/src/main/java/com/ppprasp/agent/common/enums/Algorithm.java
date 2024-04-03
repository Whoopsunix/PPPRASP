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
    RCENormal("rasp-rce", "normal"),
    RCENative("rasp-rce", "native"),

    /**
     * SQL 注入
     */
    SQLMYSQL("rasp-sql", "mysql"),

    /**
     * 文件上传
     */
    FileUpload("rasp-file-upload", "fileItem"),
    /**
     * 路径遍历
     */
    FileDirectory("rasp-file-directory", "list"),
    /**
     * 文件读取
     */
    FileREAD("rasp-file-read", "read"),

    /**
     * 内存马
     */
    MSSpringController("rasp-ms", "spring-controller"),
    MSTomcatExecutor("rasp-ms", "tomcat-executor"),
    MSTomcatListener("rasp-ms", "tomcat-listener"),
    MSTomcatServlet("rasp-ms", "tomcat-servlet"),
    MSTomcatFilter("rasp-ms", "tomcat-filter"),
    MSJettyListener("rasp-ms", "jetty-listener"),
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
