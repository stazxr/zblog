package com.github.stazxr.zblog.encryption;

import com.github.stazxr.zblog.encryption.multi.MultiEncryptor;
import com.github.stazxr.zblog.encryption.rsa.RsaEncryptor2;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MultiEncryptorTest {
    @Test
    public void testMultiEncryptor() throws IOException {
        MultiEncryptor multiEncryptor = new MultiEncryptor();
        multiEncryptor.addEncryptor("PROPS_ENCRYPTOR", new RsaEncryptor2());

        EncryptorContext.set("PROPS_ENCRYPTOR");
        String encryptText = multiEncryptor.encrypt("root");
        System.out.println(encryptText);
        String decryptTest = multiEncryptor.decrypt(encryptText);
        Assert.assertNotSame("root", decryptTest);

        // 不需要生成 rk 文件时，注释当前行
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get("D:\\test001.rk")))) {
            oos.writeObject(multiEncryptor);
        }
    }
}
