package com.github.stazxr.zblog.base.component.security.jwt;

import org.springframework.core.io.ClassPathResource;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.RSAPublicKeySpec;

/**
 * KeyPairFactory
 *
 * @author SunTao
 * @since 2022-01-19
 */
@Deprecated
class KeyPairFactory {
    private KeyStore store;

    private final Object lock = new Object();

    /**
     * 获取公私钥
     *
     * @param keyPath  the key path
     * @param keyAlias the key alias
     * @param keyPass  storepass
     * @return the key pair
     */
    KeyPair create(String keyPath, String keyAlias, String keyPass) {
        ClassPathResource resource = new ClassPathResource(keyPath);
        char[] pem = keyPass.toCharArray();
        try {
            synchronized (lock) {
                if (store == null) {
                    synchronized (lock) {
                        store = KeyStore.getInstance("jks");
                        store.load(resource.getInputStream(), pem);
                    }
                }
            }

            RSAPrivateCrtKey privateKey = (RSAPrivateCrtKey) store.getKey(keyAlias, pem);
            RSAPublicKeySpec spec = new RSAPublicKeySpec(privateKey.getModulus(), privateKey.getPublicExponent());
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(spec);
            return new KeyPair(publicKey, privateKey);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot load keys from store: " + resource, e);
        }
    }
}
