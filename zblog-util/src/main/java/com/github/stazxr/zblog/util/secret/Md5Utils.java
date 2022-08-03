package com.github.stazxr.zblog.util.secret;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类
 *
 * @author SunTao
 * @since 2021-01-25
 */
@Slf4j
public class Md5Utils {
    /**
     * 对字节进行加密
     *
     * @param buffer 加密对象
     * @return String
     * @throws Exception 加密失败
     */
    public static String getMessageDigest(byte[] buffer) throws Exception {
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

    public static String md5Hex(String message) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return hex(md.digest(message.getBytes("CP1252")));
    }

    private static String hex(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (byte b : array) {
            sb.append(Integer.toHexString(b & 0xFF | 0x100), 1, 3);
        }
        return sb.toString();
    }
}
