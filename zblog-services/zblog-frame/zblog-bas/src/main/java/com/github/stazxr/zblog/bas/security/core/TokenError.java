package com.github.stazxr.zblog.bas.security.core;

/**
 * 封装令牌校验的错误信息
 *
 * @author SunTao
 * @since 2022-03-10
 */
public enum TokenError {
    /**
     * Token 未上送
     */
    TE001("TE001", Const.NO_LOGIN, "未登录，请先登录"),

    /**
     * Token 未生效
     */
    TE002("TE002", Const.NO_LOGIN, "Token 未生效"),

    /**
     * Token 解码失败
     */
    TE003("TE003", Const.NO_LOGIN, "非法的访问令牌"), // TODO

    /**
     * Token 验证失败，系统未知错误
     */
    TE004("TE004", Const.EXCEPTION, "Token 验证失败，系统未知错误"),

    /**
     * 主体未授权
     */
    TE005("TE005", Const.NO_LOGIN, "主体未授权"), // TODO

    /**
     * 令牌已过期
     */
    TE006("TE006", Const.EXPIRED, "令牌已过期"), // TODO

    /**
     * 请求令牌与服务器端缓存的令牌对比失败
     */
    TE007("TE007", Const.EXPIRED, "请求令牌与服务器端缓存的令牌对比失败"),

    /**
     * 用户被踢出
     */
    TE008("TE008", Const.EXPIRED, "用户被踢出"),

    /**
     * IP 地址发生变化
     */
    TE009("TE009", Const.EXPIRED, "IP 地址发生变化"),

    /**
     * 续签失败
     */
    TE010("TE010", Const.EXPIRED, "续签失败"),

    /**
     * 等待令牌续签超时
     */
    TE011("TE011", Const.BUSY, "等待令牌续签超时"),

    /**
     * 触发续签限流
     */
    TE012("TE012", Const.BUSY, "触发续签限流"),

    /**
     * JWT 认证发生未知异常
     */
    TE099("TE099", Const.EXCEPTION, "用户信息认证失败，请重新登录后再试"); // TODO

    private final String code;

    private final String label;

    private final String message;

    TokenError(String code, String label, String message) {
        this.code = code;
        this.label = label;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public String getMessage() {
        return message;
    }

    public static class Const {
        /**
         * 未登录
         */
        public static final String NO_LOGIN = "SUN_TAO_001";

        /**
         * 登录状态过期
         */
        public static final String EXPIRED = "SUN_TAO_002";

        /**
         * 系统繁忙
         */
        public static final String BUSY = "SUN_TAO_003";

        /**
         * 未知异常【告警】
         */
        public static final String EXCEPTION = "SUN_TAO_999";
    }
}
