package com.github.stazxr.zblog.base.component.security.jwt;

/**
 * 封装令牌校验的错误信息
 *
 * @author SunTao
 * @since 2022-03-10
 */
public enum TokenError {
    /**
     * TE001
     */
    TE001("TE001", Const.NO_LOGIN, "非法的请求，请求未携带认证头"),

    /**
     * TE002
     */
    TE002("TE002", Const.NO_LOGIN, "非法的请求，请求未携带令牌"),

    /**
     * TE003
     */
    TE003("TE003", Const.NO_LOGIN, "令牌解码失败，一般原因为令牌不满足JWT规范或者签名不正确"),

    /**
     * TE004
     */
    TE004("TE004", Const.EXCEPTION, "令牌验证错误，发生了未考虑到的系统异常"),

    /**
     * TE005
     */
    TE005("TE005", Const.NO_LOGIN, "非法的令牌，缺失签收者或签收者不是系统内部用户"),

    /**
     * TE006
     */
    TE006("TE006", Const.EXPIRED, "令牌验证失败，请求IP与登录IP不一致"),

    /**
     * TE007
     */
    TE007("TE007", Const.EXPIRED, "令牌验证失败，请求令牌与服务器端缓存的令牌不一致"),

    /**
     * TE008
     */
    TE008("TE008", Const.EXPIRED, "令牌已过期（当前令牌不允许续签）"),

    /**
     * TE009
     */
    TE009("TE009", Const.EXPIRED, "令牌已过期（REFRESH_TOKEN 已过期）"),

    /**
     * TE010
     */
    TE010("TE010", Const.EXPIRED, "令牌已过期（续签超过最大次数）"),

    /**
     * TE011
     */
    TE011("TE011", Const.EXPIRED, "令牌续签失败"),

    /**
     * TE012
     */
    TE012("TE012", Const.BUSY, "等待令牌续签超时");

    private final String code;

    private final String label;

    private final String message;

    TokenError(String code, String label, String message) {
        this.code = code;
        this.label = label;
        this.message = message;
    }

    public String value() {
        return String.format("%s[%s] - %s", code, label, message);
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
         * 系统错误
         */
        public static final String EXCEPTION = "SUN_TAO_004";
    }
}
