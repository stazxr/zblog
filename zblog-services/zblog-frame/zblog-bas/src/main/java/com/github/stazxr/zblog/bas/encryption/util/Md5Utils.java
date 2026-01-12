package com.github.stazxr.zblog.bas.encryption.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类
 *
 * @author SunTao
 * @since 2021-01-25
 */
public class Md5Utils {
    private static final int BUFFER_SIZE = 8192;

    private static final char[] HEX = "0123456789abcdef".toCharArray();

    /**
     * 对字节进行加密
     *
     * @param buffer 加密对象
     * @return String
     * @throws Exception 加密失败
     */
    public static String md5(byte[] buffer) throws Exception {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        MessageDigest mdTemp = MessageDigest.getInstance("MD5");
        mdTemp.update(buffer);
        byte[] md = mdTemp.digest();
        int j = md.length;
        char[] str = new char[j * 2];
        int k = 0;
        for (byte byte0 : md) {
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

    public static String md5(InputStream inputStream) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;

            while ((len = inputStream.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }

            return toHex(md.digest());
        } catch (Exception e) {
            throw new RuntimeException("Calculate MD5 failed", e);
        } finally {
            closeQuietly(inputStream);
        }
    }

    public static String md5(String message) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return toHex(md.digest(message.getBytes("CP1252")));
    }

    public static String toHex(byte[] bytes) {
        char[] chars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            chars[i * 2] = HEX[v >>> 4];
            chars[i * 2 + 1] = HEX[v & 0x0F];
        }
        return new String(chars);
    }

    private static void closeQuietly(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException ignored) {}
        }
    }
}
