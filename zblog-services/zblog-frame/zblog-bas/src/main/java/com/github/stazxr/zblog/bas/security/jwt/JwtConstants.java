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

    /**
     * 资源访问级别
     */
    public static final String __RESOURCE_LEVEL = "__RESOURCE_LEVEL";

    /**
     * JWT认证时塞入的用户ID标识
     */
    public static final String __WEAK_USER_ID = "__WEAK_USER_ID";

    /**
     * Token认证异常标识
     */
    public static final String __TOKEN_ERROR = "__TOKEN_ERROR";
}
