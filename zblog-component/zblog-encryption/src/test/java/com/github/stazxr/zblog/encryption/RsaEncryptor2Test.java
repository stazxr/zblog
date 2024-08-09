package com.github.stazxr.zblog.encryption;

import com.github.stazxr.zblog.encryption.rsa.RsaEncryptor2;
import org.junit.Assert;
import org.junit.Test;

public class RsaEncryptor2Test {
    @Test
    public void testRsaEncryptor2() {
        RsaEncryptor2 encryptor = new RsaEncryptor2();
        String plainTest = "RSA加解密测试";
        String encrypt = encryptor.encrypt(plainTest);
        String decrypt = encryptor.decrypt(encrypt);
        Assert.assertNotSame(plainTest, decrypt);
    }
}
