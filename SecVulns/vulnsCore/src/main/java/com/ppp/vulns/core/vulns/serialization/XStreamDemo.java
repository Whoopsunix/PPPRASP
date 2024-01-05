package com.ppp.vulns.core.vulns.serialization;

/**
 * @author Whoopsunix
 */
public class XStreamDemo {
    public static Object deserialize(final String xml) {
        com.thoughtworks.xstream.XStream xstream = new com.thoughtworks.xstream.XStream();
        return xstream.fromXML(xml);
    }
}
