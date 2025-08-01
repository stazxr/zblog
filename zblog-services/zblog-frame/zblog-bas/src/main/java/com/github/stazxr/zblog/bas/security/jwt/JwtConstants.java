package com.github.stazxr.zblog.bas.security.jwt;

/**
 * Jwt 常量类
 *
 * @author SunTao
 * @since 2024-09-14
 */
public final class JwtConstants {
    /**
     * 用户登录IP
     */
    public static final String LOGIN_IP_KEY = "loginIp";

    /**
     * 是否允许续签
     */
    public static final String RENEW_TOKEN_KEY = "renewToken";

    /**
     * 令牌版本
     */
    public static final String JWT_VERSION_KEY = "version";

    /**
     * 新生成的令牌的请求头描述
     */
    public static final String NEW_TOKEN_HEADER = "new-token";

    /**
     * 存储前置令牌（preToken）的缓存键模板。
     */
    public static final String PTK_TOKEN_CACHE_KEY = "jwt-ptk:%s";
}
