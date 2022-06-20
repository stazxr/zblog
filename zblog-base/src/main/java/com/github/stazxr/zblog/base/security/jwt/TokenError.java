package com.github.stazxr.zblog.base.security.jwt;

/**
 * 封装令牌校验的错误信息
 *
 * @author SunTao
 * @since 2022-03-10
 */
public enum TokenError {
    /**
     * 令牌已过期 - 令牌过期，可续签
     */
    TE001("TE001"),

    /**
     * 非法的令牌, decode(token) 失败 - 视为未登录
     */
    TE002("TE002"),

    /**
     * 非法的令牌, jwt 缺失 audiences - 视为未登录
     */
    TE003("TE003"),

    /**
     * 令牌已过期, 缓存中的令牌已经失效 - 视为登录失效，不可续签
     */
    TE004("TE004"),

    /**
     * 非法的令牌, 与缓存中的令牌不一致 - 视为未登录
     */
    TE005("TE005"),

    /**
     * 令牌校验失败，发生了不可预料的错误 - 视为令牌认证系统错误
     */
    TE006("TE006"),

    /**
     * 非法的请求，请求未携带 token - 视为未登录
     */
    TE007("TE007"),

    /**
     * 非法的请求，请求未携带 Authorization - 视为未登录
     */
    TE008("TE008");

    private final String message;

    TokenError(String message) {
        this.message = message;
    }

    public String value() {
        return message;
    }
}
