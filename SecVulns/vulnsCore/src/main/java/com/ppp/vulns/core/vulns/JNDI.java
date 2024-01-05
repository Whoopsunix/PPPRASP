package com.ppp.vulns.core.vulns;

import javax.naming.InitialContext;

/**
 * @author Whoopsunix
 */
public class JNDI {

    public static void main(String[] args) throws Exception{
        lookup("ldap://127.0.0.1:1389/ju4je4");
    }
    public static Object lookup(String url) throws Exception{
        InitialContext ctx    = new InitialContext();
        return ctx.lookup(url);
    }
}
