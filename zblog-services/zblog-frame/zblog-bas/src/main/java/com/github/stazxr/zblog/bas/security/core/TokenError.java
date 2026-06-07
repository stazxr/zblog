package com.github.stazxr.zblog.bas.security.core;

import static com.github.stazxr.zblog.bas.security.core.TokenErrorType.*;

/**
 * 封装令牌校验的错误信息
 *
 * @author SunTao
 * @since 2022-03-10
 */
public enum TokenError {
    /**
     * 未上送访问令牌
     */
    TE001("TE001", TET_001, Const.NO_LOGIN),

    /**
     * 访问令牌解码失败
     */
    TE002("TE002", TET_002, Const.NO_LOGIN),

    /**
     * 账号被禁用
     */
    TE003("TE003", TET_003, Const.ACCT_DISABLED),

    /**
     * 账号被锁定
     */
    TE004("TE004", TET_003, Const.ACCT_LOCKED),

    /**
     * 账号未授权
     */
    TE005("TE005", TET_003, Const.ACCT_UNAUTHORIZED),

    /**
     * 令牌已过期
     */
    TE006("TE006", TET_004, Const.EXPIRED),

    /**
     * 令牌比对失败
     */
    TE007("TE007", TET_004, Const.EXPIRED),

    /**
     * 用户被踢出
     */
    TE008("TE008", TET_003, Const.ACCT_KICK_OUT),

    /**
     * 请求地址发生变化
     */
    TE009("TE009", TET_004, Const.IP_CHANGE),

    /**
     * 访问令牌已过期
     */
    TE010("TE010", TET_005, Const.EXPIRED),

    /**
     * 令牌认证发生未知异常
     */
    TE099("TE099", TET_006, Const.EXCEPTION);

    /**
     * 错误码
     */
    private final String code;

    /**
     * 前端业务类型
     */
    private final TokenErrorType type;

    /**
     * 国际化消息Key
     */
    private final String label;

    TokenError(String code, TokenErrorType type, String label) {
        this.code = code;
        this.type = type;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public TokenErrorType getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public static class Const {
        /**
         * 未登录
         */
        public static final String NO_LOGIN = "SUN_TAO_001";

        /**
         * 用户被禁用
         */
        public static final String ACCT_DISABLED = "SUN_TAO_002";

        /**
         * 用户被锁定
         */
        public static final String ACCT_LOCKED = "SUN_TAO_003";

        /**
         * 用户未授权
         */
        public static final String ACCT_UNAUTHORIZED = "SUN_TAO_004";

        /**
         * 用户被踢出
         */
        public static final String ACCT_KICK_OUT = "SUN_TAO_005";

        /**
         * 登录状态过期
         */
        public static final String EXPIRED = "SUN_TAO_006";

        /**
         * 登录状态过期
         */
        public static final String IP_CHANGE = "SUN_TAO_007";

        /**
         * 未知异常【告警】
         */
        public static final String EXCEPTION = "SUN_TAO_999";
    }
}
