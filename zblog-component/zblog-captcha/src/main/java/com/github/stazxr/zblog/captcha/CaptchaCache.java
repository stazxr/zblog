package com.github.stazxr.zblog.captcha;

import com.github.stazxr.zblog.util.collection.TimeMap;

/**
 * 一个用于缓存验证码的类。
 * <p>
 * 该类提供了将验证码存储到缓存中、从缓存中获取验证码以及从缓存中移除验证码的方法。
 * 缓存使用 {@link TimeMap} 类来管理验证码及其过期时间。
 * </p>
 *
 * @author SunTao
 * @since 2024-08-21
 */
public class CaptchaCache {
    /**
     * 用于存储验证码及其过期时间的缓存映射。
     */
    private static final TimeMap<String, String> CACHE_MAP = new TimeMap<>();

    /**
     * 将验证码及其过期时间放入缓存中。
     *
     * @param captchaId  验证码的唯一标识符
     * @param text       验证码的内容
     * @param expireTime 验证码的过期时间（秒）
     */
    public static void put(String captchaId, String text, int expireTime) {
        CACHE_MAP.put(captchaId, text, 1000L * expireTime);
    }

    /**
     * 从缓存中获取指定验证码的内容。
     *
     * @param captchaId 验证码的唯一标识符
     * @return 验证码的内容，如果验证码不存在或已过期则返回 {@code null}
     */
    public static String get(String captchaId) {
        return CACHE_MAP.get(captchaId);
    }

    /**
     * 从缓存中移除指定的验证码。
     *
     * @param captchaId 验证码的唯一标识符
     */
    public static void remove(String captchaId) {
        CACHE_MAP.remove(captchaId);
    }
}
