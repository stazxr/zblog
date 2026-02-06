package com.github.stazxr.zblog.bas.security.sso;

import com.github.stazxr.zblog.bas.context.util.SpringContextHolder;
import com.github.stazxr.zblog.bas.security.SecurityExtProperties;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.collection.TimeMap;

/**
 * 单点登录 (SSO) 令牌缓存，用于存储和管理单点登录（SSO）令牌。
 *
 * <p>
 * 该类使用线程安全的 {@link TimeMap} 实现了基于时间的缓存机制。
 * 每个令牌与用户的 IP 地址关联，并会在指定时间后自动过期。
 * </p>
 *
 * <p>
 * <strong>注意：</strong>需要在项目中配置 {@link SecurityExtProperties}，确保缓存时间的正确设置。
 * </p>
 *
 * <p>该缓存机制的主要目的是存储每个用户 IP 对应的单点登录令牌，并为其设置过期时间，防止滥用和提升安全性。</p>
 *
 * @author SunTao
 * @since 2024-11-16
 */
public class SsoTokenCache {
    /**
     * 单点登录令牌缓存，键为用户 IP 地址，值为对应的单点登录令牌。使用 {@link TimeMap} 实现基于时间的缓存。
     */
    private static final TimeMap<String, SsoToken> SSO_TOKEN_CACHE = new TimeMap<>("SsoTokenCacheCheckThread");

    /**
     * 将单点登录令牌存入缓存。
     * <p>
     * 如果缓存中已经存在相同用户 IP 地址的令牌，则会覆盖原有的值。
     * 缓存时间由 {@link SecurityExtProperties#getSsoTokenCacheMills()} 配置。
     * </p>
     *
     * @param ssoToken 要存储的单点登录令牌，不能为空
     */
    public static void set(SsoToken ssoToken) {
        // 参数校验
        Assert.notNull(ssoToken, "ssoToken must not be null");
        Assert.notBlank(ssoToken.getUserIp(), "User IP must not be null");

        // 获取缓存时间配置
        SecurityExtProperties properties = SpringContextHolder.getBean(SecurityExtProperties.class);
        long cacheTime = properties.getSsoTokenCacheMills();

        // 将令牌存入缓存
        SSO_TOKEN_CACHE.put(ssoToken.getUserIp(), ssoToken, cacheTime);
    }

    /**
     * 从缓存中获取指定用户 IP 地址对应的单点登录令牌。
     *
     * @param userIp 用户的 IP 地址，不能为空
     * @return SSO令牌，如果不存在或已过期，返回 {@code null}
     */
    public static SsoToken get(String userIp) {
        Assert.notBlank(userIp, "User IP must not be null");
        return SSO_TOKEN_CACHE.get(userIp);
    }

    /**
     * 从缓存中移除指定用户 IP 地址对应的单点登录令牌。
     *
     * @param userIp 用户的 IP 地址，不能为空
     */
    public static void remove(String userIp) {
        Assert.notBlank(userIp, "User IP must not be null");
        SSO_TOKEN_CACHE.remove(userIp);
    }

    /**
     * 清空整个缓存（可选功能，视需求启用）。
     * <p>
     * 注意：清空缓存会移除所有用户的全局令牌，请谨慎调用。
     * </p>
     */
    public static void clear() {
        SSO_TOKEN_CACHE.clear();
    }
}

