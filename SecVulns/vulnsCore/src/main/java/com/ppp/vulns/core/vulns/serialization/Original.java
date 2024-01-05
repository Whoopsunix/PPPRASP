package com.ppp.vulns.core.vulns.serialization;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * @author Whoopsunix
 */
public class Original {
    public static Object deserializeByte(final byte[] serialized) throws IOException, ClassNotFoundException {
        final ByteArrayInputStream in = new ByteArrayInputStream(serialized);
        return deserialize(in);
    }

    public static Object deserializeBase64(final String base64Str) throws IOException, ClassNotFoundException {
        final byte[] serialized = new sun.misc.BASE64Decoder().decodeBuffer(base64Str);
        final ByteArrayInputStream in = new ByteArrayInputStream(serialized);
        return deserialize(in);
    }

    public static Object deserializeBase64GZip(final String base64Str) throws IOException, ClassNotFoundException {
        final byte[] serialized = new sun.misc.BASE64Decoder().decodeBuffer(base64Str);
        ByteArrayInputStream byteStream = new ByteArrayInputStream(serialized);
        GZIPInputStream gzipStream = new GZIPInputStream(byteStream);
        ObjectInput objectInput = new ObjectInputStream(gzipStream);
        return objectInput.readObject();
    }

    public static Object deserialize(final InputStream in) throws ClassNotFoundException, IOException {
        final ObjectInputStream objIn = new ObjectInputStream(in);
        return objIn.readObject();
    }
}
