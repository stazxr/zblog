package com.github.stazxr.zblog.util.io;

import java.io.*;

/**
 * IoUtils
 *
 * @author SunTao
 * @since 2022-06-14
 */
public class IoUtils {
    /**
     * 将数据写入文件中
     *
     * @param data 待写入的数据
     * @param file 文件
     * @throws IOException 写入异常
     */
    public static void writeFile(String data, File file) throws IOException {
        try (OutputStream out = new FileOutputStream(file);) {
            out.write(data.getBytes());
            out.flush();
        }
    }

    /**
     * 读取文件内容
     *
     * @param file 文件
     * @return 字符串
     * @throws IOException 读取异常
     */
    public static String readFile(File file) throws IOException {
        try (InputStream in = new FileInputStream(file);ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.flush();
            return out.toString();
        }
    }
}
