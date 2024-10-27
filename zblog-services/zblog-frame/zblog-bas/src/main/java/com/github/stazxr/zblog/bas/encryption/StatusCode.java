package com.github.stazxr.zblog.bas.encryption;

import java.util.HashMap;
import java.util.Map;

/**
 * 状态码
 *
 * @author SunTao
 * @since 2024-07-28
 */
public enum StatusCode {
    /**
     * RSA 密钥对生成失败
     */
    ZENC001("RSA key pairs generation failed"),

    /**
     * RSA 加密失败
     */
    ZENC002("RSA encryption failed"),

    /**
     * RSA 解密失败
     */
    ZENC003("RSA decryption failed"),

    /**
     * 未找到对应的加解密器
     */
    ZENC004("Encryptor not found"),

    /**
     * MD5 加密失败
     */
    ZENC005("MD5 encryption failed"),

    /**
     * 不支持的操作
     */
    ZENC006("Unsupported");

    private final String message;

    /**
     * 用于缓存状态码和错误信息的映射
     */
    private static final Map<String, String> CACHE_MAP = new HashMap<>();

    /**
     * 构造函数，初始化状态码及其对应的错误信息
     *
     * @param message 错误信息
     */
    StatusCode(String message) {
        this.message = message;
    }

    /**
     * 根据状态码获取对应的错误信息
     *
     * @param code 状态码
     * @return 状态码对应的错误信息，如果状态码不存在则返回null
     */
    public static String of(String code) {
        // 如果缓存中存在该状态码，则直接返回缓存中的错误信息
        if (CACHE_MAP.containsKey(code)) {
            return CACHE_MAP.get(code);
        }

        // 遍历所有状态码，查找匹配的状态码并缓存其错误信息
        for (StatusCode value : StatusCode.values()) {
            if (value.name().equals(code)) {
                CACHE_MAP.put(value.name(), value.message);
                return value.message;
            }
        }

        // 如果状态码不存在，则返回 null
        return null;
    }
}

