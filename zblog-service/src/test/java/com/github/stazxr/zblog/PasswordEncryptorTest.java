package com.github.stazxr.zblog;

import com.github.stazxr.zblog.encryption.util.RsaUtils;
import org.junit.jupiter.api.Test;

public class PasswordEncryptorTest {
    @Test
    public void testPasswordEncryptor() throws Exception {
        String password = "111111";
        RsaUtils.RsaKeyPair rsaKeyPair = RsaUtils.generateKeyPair(1024);
        System.out.println(rsaKeyPair.getPublicKey());
        System.out.println(rsaKeyPair.getPrivateKey());
        // String pubKeyBase64 = FileUtils.readFileFromStream(new ClassPathResource("pub.key").getInputStream());
        String encrypt = RsaUtils.encryptByPublicKey(rsaKeyPair.getPublicKey(), password);
        System.out.println(encrypt);

        // String priKeyBase64 = FileUtils.readFileFromStream(new ClassPathResource("pri.key").getInputStream());
        String decrypt = RsaUtils.decryptByPrivateKey(rsaKeyPair.getPrivateKey(), encrypt);
        System.out.println(decrypt);
    }
}
