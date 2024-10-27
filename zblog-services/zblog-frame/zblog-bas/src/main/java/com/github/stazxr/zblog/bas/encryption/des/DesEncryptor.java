package com.github.stazxr.zblog.bas.encryption.des;

import com.github.stazxr.zblog.bas.encryption.DecryptException;
import com.github.stazxr.zblog.bas.encryption.Encryptor;
import com.github.stazxr.zblog.bas.encryption.EncryptorException;

/**
 * DES 加解密 TODO 未实现 @2024-10-22
 *
 * @author SunTao
 * @since 2024-07-28
 */
public class DesEncryptor implements Encryptor {
    private static final long serialVersionUID = -6697177566899786323L;

    /**
     * 加密方法
     *
     * @param plainText 明文字符串
     * @return 加密后的密文字符串
     * @throws EncryptorException 如果加密过程中出现错误
     */
    @Override
    public String encrypt(String plainText) throws EncryptorException {
        return null;
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
        return null;
    }
}
