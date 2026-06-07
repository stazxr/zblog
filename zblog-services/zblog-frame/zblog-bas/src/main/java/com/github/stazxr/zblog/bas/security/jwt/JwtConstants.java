package com.github.stazxr.zblog.bas.security.jwt;

/**
 * Jwt 常量类
 *
 * @author SunTao
 * @since 2024-09-14
 */
public final class JwtConstants {
    /**
     * 访问令牌名
     */
    public static final String ACCESS_TOKEN = "access_token";

    /**
     * 刷新令牌名
     */
    public static final String REFRESH_TOKEN = "refresh_token";

    /**
     * 用户登录IP
     */
    public static final String LOGIN_IP_KEY = "loginIp";

    /**
     * 新生成的令牌的请求头描述
     */
    public static final String X_TOKEN_STATUS = "x-token-status";
}
