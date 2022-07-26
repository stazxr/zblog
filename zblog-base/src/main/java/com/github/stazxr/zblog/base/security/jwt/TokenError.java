package com.github.stazxr.zblog.base.security.jwt;

/**
 * 封装令牌校验的错误信息
 *
 *   整理来说：TE001 - 可尝试续签；TE004 - 不可续签；TE006 - 系统异常；其他 - 请求/令牌不标准，均视为恶意请求，返回401
 *
 * @author SunTao
 * @since 2022-03-10
 */
public enum TokenError {
    /**
     * 令牌已过期，可尝试续签，不保证成功
     */
    TE001("TE001"),

    /**
     * 令牌解析失败
     */
    TE002("TE002"),

    /**
     * 令牌不标准
     */
    TE003("TE003"),

    /**
     * 令牌已过期，不可续签
     */
    TE004("TE004"),

    /**
     * 令牌被篡改, 与缓存中的令牌不一致
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
