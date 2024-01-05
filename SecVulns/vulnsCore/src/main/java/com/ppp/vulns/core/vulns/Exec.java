package com.ppp.vulns.core.vulns;

import sun.misc.Unsafe;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Whoopsunix
 *
 * 命令执行漏洞
 */
public class Exec {
    /**
     * java.lang.Runtime
     * @param str
     * @return
     * @throws Exception
     */
    public static String runtime(String str) throws Exception {
        InputStream inputStream = null;
        String[] cmd = osChoose(str);
        if (cmd != null) {
            inputStream = Runtime.getRuntime().exec(cmd).getInputStream();
        }
        return new Scanner(inputStream).useDelimiter("\\A").next();
    }

    /**
     * 线程注入
     * @param str
     * @return
     * @throws Exception
     */
    public static String thread(String str) throws Exception {
        AtomicReference<InputStream> inputStreamRef = new AtomicReference<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String[] cmd = osChoose(str);
                    InputStream inputStream = Runtime.getRuntime().exec(cmd).getInputStream();
                    inputStreamRef.set(inputStream);
                } catch (Exception e) {
                    // Handle the exception
                }
            }
        });
        thread.start();
        thread.join();

        return new Scanner(inputStreamRef.get()).useDelimiter("\\A").next();
    }

    /**
     * java.lang.ProcessImpl
     * @param str
     * @return
     * @throws Exception
     */
    public static String processImpl(String str) throws Exception {
        InputStream inputStream = null;

        String[] cmd = osChoose(str);
        Class<?> cls = Class.forName("java.lang.ProcessImpl");
        Method method = cls.getDeclaredMethod("start", String[].class, Map.class, String.class, ProcessBuilder.Redirect[].class, boolean.class);
        method.setAccessible(true);
        Process e = (Process) method.invoke(null, cmd, null, ".", null, true);
        inputStream = e.getInputStream();

        return new Scanner(inputStream).useDelimiter("\\A").next();
    }

    /**
     * java.lang.ProcessBuilder
     * @param str
     * @return
     * @throws Exception
     */
    public static String processBuilder(String str) throws Exception{
        InputStream inputStream = null;

        Class<?> cls = Class.forName("java.lang.ProcessBuilder");
        Constructor<?> constructor = cls.getDeclaredConstructor(String[].class);
        constructor.setAccessible(true);
        String[] cmd = osChoose(str);
        ProcessBuilder pb = (ProcessBuilder) constructor.newInstance(new Object[]{cmd});
        Method method = cls.getDeclaredMethod("start");
        method.setAccessible(true);
        inputStream = ((Process) method.invoke(pb)).getInputStream();

        return new Scanner(inputStream).useDelimiter("\\A").next();
    }

    /**
     * java.lang.UNIXProcess
     * @param str
     * @return
     * @throws Exception
     */
    public static String processImplUnixProcess(String str) throws Exception {
        InputStream inputStream = null;
        String[] cmd = osChoose(str);
        Class<?> processClass = null;
        try {
            processClass = Class.forName("java.lang.UNIXProcess");
        } catch (ClassNotFoundException e) {
            processClass = Class.forName("java.lang.ProcessImpl");
        }
        Constructor<?> constructor = processClass.getDeclaredConstructors()[0];
        constructor.setAccessible(true);

        // arguments
        byte[][] args = new byte[cmd.length - 1][];
        int size = args.length;

        for (int i = 0; i < args.length; i++) {
            args[i] = cmd[i + 1].getBytes();
            size += args[i].length;
        }

        byte[] argBlock = new byte[size];
        int i = 0;
        for (byte[] arg : args) {
            System.arraycopy(arg, 0, argBlock, i, arg.length);
            i += arg.length + 1;
        }
        int[] envc = new int[1];
        int[] std_fds = new int[]{-1, -1, -1};


        Object object = constructor.newInstance(
                toCString(cmd[0]), argBlock, args.length,
                null, envc[0], null, std_fds, false
        );
        // 获取命令执行的InputStream
        Method inMethod = object.getClass().getDeclaredMethod("getInputStream");
        inMethod.setAccessible(true);
        inputStream = (InputStream) inMethod.invoke(object);

        return new Scanner(inputStream).useDelimiter("\\A").next();
    }

    /**
     * java.lang.UNIXProcess native
     * @param str
     * @return
     * @throws Exception
     */
    public static String processImplUnixProcessByUnsafeNative(String str) throws Exception {
        InputStream inputStream = null;

        String[] cmd = osChoose(str);
        Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafeField.get(null);
        Class processClass = null;

        try {
            processClass = Class.forName("java.lang.UNIXProcess");
        } catch (ClassNotFoundException e) {
            processClass = Class.forName("java.lang.ProcessImpl");
        }
        Object processObject = unsafe.allocateInstance(processClass);

        // arguments
        byte[][] args = new byte[cmd.length - 1][];
        int size = args.length;

        for (int i = 0; i < args.length; i++) {
            args[i] = cmd[i + 1].getBytes();
            size += args[i].length;
        }

        byte[] argBlock = new byte[size];
        int i = 0;

        for (byte[] arg : args) {
            System.arraycopy(arg, 0, argBlock, i, arg.length);
            i += arg.length + 1;
        }

        int[] envc = new int[1];
        int[] std_fds = new int[]{-1, -1, -1};
        Field launchMechanismField = processClass.getDeclaredField("launchMechanism");
        Field helperpathField = processClass.getDeclaredField("helperpath");
        launchMechanismField.setAccessible(true);
        helperpathField.setAccessible(true);
        Object launchMechanismObject = launchMechanismField.get(processObject);
        byte[] helperpathObject = (byte[]) helperpathField.get(processObject);

        int ordinal = (int) launchMechanismObject.getClass().getMethod("ordinal").invoke(launchMechanismObject);

        Method forkMethod = processClass.getDeclaredMethod("forkAndExec", new Class[]{
                int.class, byte[].class, byte[].class, byte[].class, int.class,
                byte[].class, int.class, byte[].class, int[].class, boolean.class
        });

        forkMethod.setAccessible(true);

        int pid = (int) forkMethod.invoke(processObject, new Object[]{
                ordinal + 1, helperpathObject, toCString(cmd[0]), argBlock, args.length,
                null, envc[0], null, std_fds, false
        });

        Method initStreamsMethod = processClass.getDeclaredMethod("initStreams", int[].class);
        initStreamsMethod.setAccessible(true);
        initStreamsMethod.invoke(processObject, std_fds);


        Method getInputStreamMethod = processClass.getMethod("getInputStream");
        getInputStreamMethod.setAccessible(true);
        inputStream = (InputStream) getInputStreamMethod.invoke(processObject);

        return new Scanner(inputStream).useDelimiter("\\A").next();
    }

    public static byte[] toCString(String s) {
        if (s == null)
            return null;
        byte[] bytes = s.getBytes();
        byte[] result = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0,
                result, 0,
                bytes.length);
        result[result.length - 1] = (byte) 0;
        return result;
    }

    /**
     * 构造对应系统的命令
     * @param str
     * @return
     * @throws Exception
     */
    public static String[] osChoose(String str) throws Exception {
        String[] cmd = null;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            cmd = new String[]{"cmd.exe", "/c", str};
        } else {
            cmd = new String[]{"/bin/sh", "-c", str};
        }
        return cmd;
    }



}
