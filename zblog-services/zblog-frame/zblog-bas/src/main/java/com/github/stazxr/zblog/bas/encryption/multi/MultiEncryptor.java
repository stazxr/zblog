package com.github.stazxr.zblog.bas.encryption.multi;

import com.github.stazxr.zblog.bas.encryption.DecryptException;
import com.github.stazxr.zblog.bas.encryption.Encryptor;
import com.github.stazxr.zblog.bas.encryption.EncryptorContext;
import com.github.stazxr.zblog.bas.encryption.EncryptorException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 复合加解密工具
 * 该类实现了加解密接口，并允许通过添加不同的加解密器来扩展功能。
 *
 * @author SunTao
 * @since 2024-07-25
 */
public class MultiEncryptor implements Encryptor {
    private static final long serialVersionUID = -1104396280226104863L;

    /**
     * 存储不同的加解密器实例的映射表
     */
    private final Map<String, Encryptor> ENCRYPTOR_MAP = new LinkedHashMap<>();

    /**
     * 加密方法
     *
     * @param plainText 明文字符串
     * @return 加密后的密文字符串
     * @throws EncryptorException 如果加密过程中出现错误
     */
    @Override
    public String encrypt(String plainText) throws EncryptorException {
        String encryptorName = EncryptorContext.get();
        Encryptor encryptor = getEncryptor(encryptorName);
        return encryptor.encrypt(plainText);
    }

    /**
     * 解密方法
     *
     * @param cipherText 密文字符串
     * @return 解密后的明文字符串
     * @throws DecryptException 如果解密过程中出现错误
     */
    @Override
    public String decrypt(String cipherText) throws DecryptException {
        String encryptorName = EncryptorContext.get();
        Encryptor encryptor = getEncryptor(encryptorName);
        return encryptor.decrypt(cipherText);
    }

    /**
     * 获取加解密器
     *
     * @param encryptorName 加解密器的名称
     * @return encryptor 加解密器实例
     */
    public Encryptor getEncryptor(String encryptorName) {
        // 从映射表中获取加解密器实例
        Encryptor encryptor = ENCRYPTOR_MAP.get(encryptorName);
        if (encryptor == null) {
            throw new DecryptException("ZENC004");
        }
        return encryptor;
    }

    /**
     * 添加新的加解密器
     *
     * @param encryptorName 加解密器的名称
     * @param encryptor 加解密器实例
     */
    public void addEncryptor(String encryptorName, Encryptor encryptor) {
        // 将新的加解密器添加到映射表中
        ENCRYPTOR_MAP.put(encryptorName, encryptor);
    }

    /**
     * 移除指定的加解密器
     *
     * @param encryptorName 要移除的加解密器的名称
     */
    public void removeEncryptor(String encryptorName) {
        // 从映射表中移除指定名称的加解密器
        ENCRYPTOR_MAP.remove(encryptorName);
    }
}
