package com.github.stazxr.zblog.base.domain.enums;

/**
 * 缓存枚举
 *
 * @author SunTao
 * @since 2026-01-15
 */
public enum CacheKey {
    /**
     * 邮箱验证码，
     */
    EMAIL_CODE("emailCode:%s", 5 * 60 * 1000);

    /**
     * 缓存键
     */
    private final String key;

    /**
     * 缓存时间，单位毫秒
     */
    private final long ttl;

    CacheKey(String key, long ttl) {
        this.key = key;
        this.ttl = ttl;
    }

    public String getKey() {
        return key;
    }

    public long getTtl() {
        return ttl;
    }
}
