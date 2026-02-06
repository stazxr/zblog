package com.github.stazxr.zblog.bas.encryption.rsa;

import com.github.stazxr.zblog.bas.encryption.DecryptException;
import com.github.stazxr.zblog.bas.encryption.EncryptException;
import com.github.stazxr.zblog.bas.encryption.EncryptionErrorCode;
import com.github.stazxr.zblog.bas.encryption.Encryptor;
import com.github.stazxr.zblog.bas.encryption.util.RsaUtils;

/**
 * 该类实现了 Encryptor 接口，使用 RSA 算法进行加解密操作。
 * 使用公钥进行加密，私钥进行解密（常用）。
 *
 * @author SunTao
 * @since 2024-07-28
 */
public class RsaEncryptor2 implements Encryptor {
    private static final long serialVersionUID = 3170916828486300106L;

    /**
     * 存储 RSA 公钥
     */
    private String publicKey;

    /**
     * 存储 RSA 私钥
     */
    private final String privateKey;

    /**
     * 在创建实例时生成 RSA 密钥对，并初始化公钥和私钥。
     */
    public RsaEncryptor2() {
        try {
            RsaUtils.RsaKeyPair rsaKeyPair = RsaUtils.generateKeyPair();
            privateKey = rsaKeyPair.getPrivateKey();
            publicKey = rsaKeyPair.getPublicKey();
        } catch (Exception e) {
            throw new EncryptException(EncryptionErrorCode.SENCYA000, e);
        }
    }

    /**
     * 使用 RSA 公钥加密明文。
     *
     * @param plainText 明文字符串
     * @return 加密后的密文字符串
     * @throws EncryptException 如果加密过程中出现错误
     */
    @Override
    public String encrypt(String plainText) throws EncryptException {
        try {
            // 使用公钥加密明文
            return RsaUtils.encryptByPublicKey(publicKey, plainText);
        } catch (Exception e) {
            throw new EncryptException(EncryptionErrorCode.SENCYA000, e);
        }
    }

    /**
     * 使用 RSA 私钥解密密文。
     *
     * @param cipherText 密文字符串
     * @return 解密后的明文字符串
     * @throws DecryptException 如果解密过程中出现错误
     */
    @Override
    public String decrypt(String cipherText) throws DecryptException {
        try {
            // 使用私钥解密密文
            return RsaUtils.decryptByPrivateKey(privateKey, cipherText);
        } catch (Exception e) {
            throw new DecryptException(EncryptionErrorCode.SENCYA000, e);
        }
    }

    /**
     * 将公钥设为 null，用于隐藏公钥。
     * 调用此方法后，将无法再使用该实例进行加密操作，因为公钥被清除。
     */
    public void maskKey() {
        this.publicKey = null;
    }
}
