package com.github.stazxr.zblog.encryption.md5;

import com.github.stazxr.zblog.encryption.DecryptException;
import com.github.stazxr.zblog.encryption.EncryptException;
import com.github.stazxr.zblog.encryption.Encryptor;
import com.github.stazxr.zblog.encryption.EncryptorException;
import com.github.stazxr.zblog.encryption.util.Md5Utils;

/**
 * MD5 加密
 *
 * @author SunTao
 * @since 2024-07-28
 */
public class Md5Encryptor implements Encryptor {
    private static final long serialVersionUID = 4996267291517076719L;

    /**
     * 加密方法
     *
     * @param plainText 明文字符串
     * @return 加密后的密文字符串
     * @throws EncryptorException 如果加密过程中出现错误
     */
    @Override
    public String encrypt(String plainText) throws EncryptorException {
        try {
            return Md5Utils.md5Hex(plainText);
        } catch (Exception e) {
            throw new EncryptException("ZENC005", e);
        }
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
        throw new DecryptException("ZENC006");
    }
}
