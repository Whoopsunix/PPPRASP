package com.ppprasp.agent.common;

import com.ppprasp.agent.common.enums.Algorithm;
import com.ppprasp.agent.common.enums.Status;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Whoopsunix
 * <p>
 * RASP 算法配置
 *  todo 动态配置
 */
public class RASPConfig {
    public static void main(String[] args) {
//        String is = RASPConfig.isCheck("rasp-rce", "normal");
//        System.out.println(is);

        Status status = getAlgoStatus(Algorithm.Deserialization.getAlgoId(), Algorithm.Deserialization.getAlgoName());
        System.out.println(status);
    }

    private static String configPath = "raspConfig.yml";

    public static Status getAlgoStatus(String algoId, String algoName) {
        Map<String, Object> yamlData = new RASPConfig().loadYamlConfig(configPath);
        LinkedHashMap algoConfig = (LinkedHashMap) yamlData.get(algoId);

        // 是否开启
        Boolean isEnable = (Boolean) algoConfig.get("enable");

        if (!isEnable)
            return null;

        // 获取算法配置
        LinkedHashMap algorithms = (LinkedHashMap) algoConfig.get("algorithms");
        String status = (String) algorithms.get(algoName);

        if (status == null)
            return null;

        if (status.equalsIgnoreCase(Status.OPEN.getDescription())) {
            return Status.OPEN;
        } else if (status.equalsIgnoreCase(Status.CLOSE.getDescription())) {
            return Status.CLOSE;
        } else if (status.equalsIgnoreCase(Status.LOG.getDescription())) {
            return Status.LOG;
        }

        return null;
    }

    public Map<String, Object> loadYamlConfig(String filePath) {
        Yaml yaml = new Yaml();
        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filePath);
        Map<String, Object> yamlData = yaml.load(inputStream);

        return yamlData;
    }

    /**
     * 是否要检查，规划三种模式
     * todo 目前只做 block
     * block: 阻断
     * log: 不阻断只记录日志
     * ignore: 不阻断不记录日志
     *
     * @param hookName
     * @param algorithmName
     * @return
     */
    public static String isCheck(String hookName, String algorithmName) {
        Map<String, Object> yamlData = new RASPConfig().loadYamlConfig();
        // 获取 Hook 配置
        ArrayList hookList = (ArrayList) yamlData.get("hook");
        for (Object hook : hookList) {
            LinkedHashMap hookMap = (LinkedHashMap) hook;
            // 是否为需要检测的算法
            if (!(hookMap.get("id").toString().equalsIgnoreCase(hookName))) {
                continue;
            }
            if (!(Boolean) hookMap.get("enable")) {
                // false 不检测
                return "ignore";
            }

            // 获取算法配置
            ArrayList algorithms = (ArrayList) ((LinkedHashMap) hook).get("algorithms");
            for (Object algorithm : algorithms) {
                LinkedHashMap algorithmMap = (LinkedHashMap) algorithm;
                if (algorithmMap.get("id").toString().equalsIgnoreCase(algorithmName)) {
                    return (String) algorithmMap.get("action");
                }
            }
        }

        // 默认要检查
        return "block";
    }

    public Map<String, Object> loadYamlConfig() {
        Yaml yaml = new Yaml();
        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(configPath);
        Map<String, Object> yamlData = yaml.load(inputStream);

        return yamlData;
    }
}
