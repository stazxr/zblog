package com.github.stazxr.zblog.bas.encryption.util;

import java.util.Base64;

/**
 * Base64 工具类
 *
 * @author SunTao
 * @since 2024年07月13日
 */
public class Base64Util {
    /**
     * Base64 编码
     *
     * @param data 要编码的字节数组
     * @return 编码后的字符串
     */
    public static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Base64 解码
     *
     * @param base64 编码后的字符串
     * @return 解码后的字节数组
     */
    public static byte[] decode(String base64) {
        return Base64.getDecoder().decode(base64);
    }
}
