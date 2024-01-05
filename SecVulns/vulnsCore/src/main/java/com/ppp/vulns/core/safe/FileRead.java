package com.ppp.vulns.core.safe;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Whoopsunix
 */
public class FileRead {
    /**
     * 对照组
     */
    public String read_InputStreamReader_text(String str) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(str.getBytes());

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        StringBuilder content = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            content.append(line).append("\n");
        }
        bufferedReader.close();
        return content.toString();
    }
}
