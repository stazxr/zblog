package com.github.stazxr.zblog.bas.captcha;

import com.github.stazxr.zblog.bas.cache.util.GlobalCache;

import java.util.concurrent.TimeUnit;

/**
 * 验证码缓存类。
 *
 * @author SunTao
 * @since 2024-08-21
 */
public class CaptchaCache {
    public static final String CAPTCHA_CACHE_KEY = "captcha:";

    /**
     * 将验证码及其过期时间放入缓存中。
     *
     * @param captchaId  验证码的唯一标识符
     * @param text       验证码的内容
     * @param expireTime 验证码的过期时间（秒）
     */
    public static void put(String captchaId, String text, int expireTime) {
        GlobalCache.put(cacheKey(captchaId), text, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 从缓存中获取指定验证码的内容。
     *
     * @param captchaId 验证码的唯一标识符
     * @return 验证码的内容，如果验证码不存在或已过期则返回 {@code null}
     */
    public static String get(String captchaId) {
        return GlobalCache.get(cacheKey(captchaId));
    }

    /**
     * 从缓存中移除指定的验证码。
     *
     * @param captchaId 验证码的唯一标识符
     */
    public static void remove(String captchaId) {
        GlobalCache.remove(cacheKey(captchaId));
    }

    private static String cacheKey(String captchaId) {
        return CAPTCHA_CACHE_KEY.concat(captchaId);
    }
}
