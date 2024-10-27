package com.github.stazxr.zblog.bas.encryption.md5;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class Md5EncryptorTest {
    @Test
    public void testMd5Encryptor() {
        Md5Encryptor encryptor = new Md5Encryptor();
        String plainTest = "MD5加解密测试";
        String encrypt1 = encryptor.encrypt(plainTest);
        String encrypt2 = encryptor.encrypt(plainTest);
        Assert.assertNotSame(encrypt1, encrypt2);

        try {
            String decrypt = encryptor.decrypt(encrypt1);
            System.out.println(decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
