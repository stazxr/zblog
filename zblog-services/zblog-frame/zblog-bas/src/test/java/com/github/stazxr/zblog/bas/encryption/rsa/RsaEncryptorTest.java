package com.github.stazxr.zblog.bas.encryption.rsa;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
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
