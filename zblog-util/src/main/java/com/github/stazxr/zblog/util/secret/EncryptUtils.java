package com.github.stazxr.zblog.util.secret;

import cn.hutool.core.codec.Base64;
import com.github.stazxr.zblog.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;

/**
 * 加解密工具类
 *
 * @author SunTao
 * @since 2022-01-27
 */
public class EncryptUtils {
    /**
     * 向量值
     */
    private static final String VECTOR_KEY = "PAsSw0rd";

    private static Cipher cipher;

    private static final IvParameterSpec IV = new IvParameterSpec(VECTOR_KEY.getBytes(StandardCharsets.UTF_8));

    /**
     * 对称加密
     *
     * @param source 加密字符串
     * @return encrypt str
     * @throws Exception encrypt failed
     */
    public static String desEncrypt(String source) throws Exception {
        String validSymbol = " ";
        if (StringUtils.isBlank(source) || source.contains(validSymbol)) {
            throw new IllegalArgumentException("encrypt str must not be blank and not contains space");
        }

        DESKeySpec desKeySpec = getDesKeySpec();
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IV);
        byte[] bytes = cipher.doFinal(source.getBytes(StandardCharsets.UTF_8));
        String hexStr = byte2hex(bytes).toUpperCase();
        return Base64.encode(hexStr);
    }

    /**
     * 对称解密
     *
     * @param source 加密字符串
     * @return decrypt str
     * @throws Exception encrypt failed
     */
    public static String desDecrypt(String source) throws Exception {
        if (StringUtils.isBlank(source)) {
            throw new IllegalArgumentException("decrypt str must not be blank");
        }

        byte[] src = hex2byte(Base64.decode(source));
        DESKeySpec desKeySpec = getDesKeySpec();
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IV);
        byte[] retByte = cipher.doFinal(src);
        return new String(retByte);
    }

    private static DESKeySpec getDesKeySpec() throws Exception {
        cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        // 盐值
        String slatKey = "zh0ngGuO";
        return new DESKeySpec(slatKey.getBytes(StandardCharsets.UTF_8));
    }

    private static String byte2hex(byte[] inStr) {
        String stmp;
        StringBuilder out = new StringBuilder(inStr.length * 2);
        for (byte b : inStr) {
            stmp = Integer.toHexString(b & 0xFF);
            if (stmp.length() == 1) {
                // 如果是 0 至 F 的单位字符串，则添加 0
                out.append("0").append(stmp);
            } else {
                out.append(stmp);
            }
        }
        return out.toString();
    }

    private static byte[] hex2byte(byte[] b) {
        int size = 2;
        if ((b.length % size) != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += size) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }
}
