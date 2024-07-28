package com.github.stazxr.zblog.encryption;

import java.io.Serializable;

/**
 * 加密解密接口。
 *
 * @author SunTao
 * @since 2024-07-25
 */
public interface Encryptor extends Serializable {
    /**
     * 加密方法
     *
     * @param plainText 明文字符串
     * @return 加密后的密文字符串
     * @throws EncryptorException 如果加密过程中出现错误
     */
    String encrypt(String plainText) throws EncryptorException;

    /**
     * 解密方法
     *
     * @param cipherText 密文字符串
     * @return 解密后的明文字符串
     * @throws DecryptException 如果解密过程中出现错误
     */
    String decrypt(String cipherText) throws DecryptException;
}
