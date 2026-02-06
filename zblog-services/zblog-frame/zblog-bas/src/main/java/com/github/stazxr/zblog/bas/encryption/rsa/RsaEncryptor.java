package com.github.stazxr.zblog.bas.encryption.rsa;

import com.github.stazxr.zblog.bas.encryption.DecryptException;
import com.github.stazxr.zblog.bas.encryption.EncryptException;
import com.github.stazxr.zblog.bas.encryption.EncryptionErrorCode;
import com.github.stazxr.zblog.bas.encryption.Encryptor;
import com.github.stazxr.zblog.bas.encryption.util.RsaUtils;

/**
 * 该类实现了 Encryptor 接口，使用 RSA 算法进行加解密操作。
 * 使用私钥进行加密，公钥进行解密。
 *
 * @author SunTao
 * @since 2024-07-28
 */
public class RsaEncryptor implements Encryptor {
    private static final long serialVersionUID = -2350074998625152185L;

    /**
     * 存储 RSA 公钥
     */
    private final String publicKey;

    /**
     * 存储 RSA 私钥
     */
    private String privateKey;

    /**
     * 在创建实例时生成 RSA 密钥对，并初始化公钥和私钥。
     */
    public RsaEncryptor() {
        try {
            RsaUtils.RsaKeyPair rsaKeyPair = RsaUtils.generateKeyPair();
            privateKey = rsaKeyPair.getPrivateKey();
            publicKey = rsaKeyPair.getPublicKey();
        } catch (Exception e) {
            throw new EncryptException(EncryptionErrorCode.SENCYA000, e);
        }
    }

    /**
     * 使用 RSA 私钥加密明文。
     *
     * @param plainText 明文字符串
     * @return 加密后的密文字符串
     * @throws EncryptException 如果加密过程中出现错误
     */
    @Override
    public String encrypt(String plainText) throws EncryptException {
        try {
            // 使用私钥加密明文
            return RsaUtils.encryptByPrivateKey(privateKey, plainText);
        } catch (Exception e) {
            throw new EncryptException(EncryptionErrorCode.SENCYA000, e);
        }
    }

    /**
     * 使用 RSA 公钥解密密文。
     *
     * @param cipherText 密文字符串
     * @return 解密后的明文字符串
     * @throws DecryptException 如果解密过程中出现错误
     */
    @Override
    public String decrypt(String cipherText) throws DecryptException {
        try {
            // 使用公钥解密密文
            return RsaUtils.decryptByPublicKey(publicKey, cipherText);
        } catch (Exception e) {
            throw new DecryptException(EncryptionErrorCode.SENCYA000, e);
        }
    }

    /**
     * 将私钥设为 null，用于隐藏私钥。
     * 调用此方法后，将无法再使用该实例进行加密操作，因为私钥被清除。
     */
    public void maskKey() {
        this.privateKey = null;
    }
}

