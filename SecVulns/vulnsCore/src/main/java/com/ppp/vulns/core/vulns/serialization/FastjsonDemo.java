package com.ppp.vulns.core.vulns.serialization;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author Whoopsunix
 */
public class FastjsonDemo {
    public static Object parseObject(String json){
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject;
    }
}
