package com.ppprasp.agent.check;

/**
 * @author Whoopsunix
 */
public class SqlChecker {
    /**
     * todo sql 注入检查
     */
    public static boolean isDangerous(String sql) {
        System.out.println(sql);

        // todo 暂时全部返回 true 拦截
        return true;

//        return false;
    }
}
