package com.github.stazxr.zblog.base.security.jwt;

/**
 * 封装令牌校验的错误信息
 *
 * @author SunTao
 * @since 2022-03-10
 */
public enum TokenError {
    /**
     * 令牌已过期
     */
    EXPIRED("TE001", "令牌已过期，请重新认证"),

    /**
     * 非法的令牌, decode(token) 失败
     */
    VALID("TE002", "非法的令牌"),

    /**
     * 非法的令牌, jwt 缺失 audiences
     */
    MISS_AUDIENCE("TE003", "非法的令牌"),

    /**
     * 令牌已过期, 缓存中的令牌已经失效
     */
    MISS_CACHE("TE004", "令牌已过期，请重新认证"),

    /**
     * 非法的令牌, 与缓存中的令牌不一致
     */
    NOT_MATCH("TE005", "非法的令牌"),

    /**
     * 令牌校验失败，发生了不可预料的错误
     */
    UNKNOWN_ERROR("TE006", "令牌校验失败，请联系管理员"),

    /**
     * 非法的请求，请求未携带 token
     */
    MISS_TOKEN("TE007", "非法的请求"),

    /**
     * 非法的请求
     */
    VALID_REQUEST("TE008", "非法的请求");

    private final String code;

    private final String message;

    TokenError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String getCode() {
        return code;
    }

    private String getMessage() {
        return message;
    }

    public String value() {
        return getMessage().concat("[").concat(getCode()).concat("]");
    }
}
