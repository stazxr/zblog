package com.github.stazxr.zblog.bas.encryption.multi;

import com.github.stazxr.zblog.bas.encryption.EncryptorContext;
import com.github.stazxr.zblog.bas.encryption.rsa.RsaEncryptor2;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Ignore
public class MultiEncryptorTest {
    /**
     * 生成 Props 配置库密钥文件
     *
     * @throws IOException 生成 Props 配置库密钥文件失败
     */
    @Test
    public void generatePropsEncryptorFile() throws IOException {
        String encryptorKey = "PROPS_ENCRYPTOR"; // 配置库加密规则 Key
        MultiEncryptor multiEncryptor = new MultiEncryptor();
        multiEncryptor.addEncryptor(encryptorKey, new RsaEncryptor2());

        EncryptorContext.set(encryptorKey);
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
