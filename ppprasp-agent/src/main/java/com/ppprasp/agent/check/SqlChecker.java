package com.ppprasp.agent.check;

/**
 * @author Whoopsunix
 *
 * sql注入检测
 */
public class SqlChecker {
    /**
     * todo sql 注入检查
     */
    public static boolean isDangerous(String sql) {

        // todo 暂时全部返回 true 拦截，需要考察一下 词法\语法 分析引擎的能力后再考虑拓展
        return true;

//        return false;
    }
}
