package com.github.stazxr.zblog.encryption;

import com.github.stazxr.zblog.encryption.rsa.RsaEncryptor;
import org.junit.Assert;
import org.junit.Test;

public class RsaEncryptorTest {
    @Test
    public void testRsaEncryptor() {
        RsaEncryptor encryptor = new RsaEncryptor();
        String plainTest = "RSA加解密测试";
        String encrypt = encryptor.encrypt(plainTest);
        String decrypt = encryptor.decrypt(encrypt);
        Assert.assertNotSame(plainTest, decrypt);
    }
}
