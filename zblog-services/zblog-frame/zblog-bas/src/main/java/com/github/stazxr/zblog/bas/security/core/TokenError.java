package com.github.stazxr.zblog.bas.security.core;

import com.github.stazxr.zblog.bas.i18n.I18nUtils;

import static com.github.stazxr.zblog.bas.rest.ResultType.JWT_FAILED_WITH_REFRESH;
import static com.github.stazxr.zblog.bas.rest.ResultType.JWT_FAILED_WITHOUT_REFRESH;

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
    TE001("TE001", JWT_FAILED_WITH_REFRESH, Const.NO_LOGIN),

    /**
     * 访问令牌解码失败
     */
    TE002("TE002", JWT_FAILED_WITHOUT_REFRESH, Const.NO_LOGIN),

    /**
     * 账号被禁用
     */
    TE003("TE003", JWT_FAILED_WITHOUT_REFRESH, Const.ACCT_DISABLED),

    /**
     * 账号被锁定
     */
    TE004("TE004", JWT_FAILED_WITHOUT_REFRESH, Const.ACCT_LOCKED),

    /**
     * 账号未授权
     */
    TE005("TE005", JWT_FAILED_WITHOUT_REFRESH, Const.ACCT_UNAUTHORIZED),

    /**
     * 令牌比对失败
     */
    TE006("TE006", JWT_FAILED_WITHOUT_REFRESH, Const.EXPIRED),

    /**
     * 用户被踢出
     */
    TE007("TE007", JWT_FAILED_WITHOUT_REFRESH, Const.ACCT_KICK_OUT),

    /**
     * 请求地址发生变化
     */
    TE008("TE008", JWT_FAILED_WITHOUT_REFRESH, Const.IP_CHANGE),

    /**
     * 令牌已过期
     */
    TE009("TE009", JWT_FAILED_WITHOUT_REFRESH, Const.EXPIRED),

    /**
     * 访问令牌已过期
     */
    TE010("TE010", JWT_FAILED_WITH_REFRESH, Const.EXPIRED),

    /**
     * 令牌认证发生未知异常
     */
    TE099("TE099", JWT_FAILED_WITHOUT_REFRESH, Const.EXCEPTION);

    /**
     * 错误码
     */
    private final String code;

    /**
     * 前端业务类型
     */
    private final String type;

    /**
     * 国际化消息Key
     */
    private final String label;

    TokenError(String code, String type, String label) {
        this.code = code;
        this.type = type;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public String getMessage() {
        return code + ":" + I18nUtils.getMessage(label);
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
         * 未知异常
         */
        public static final String EXCEPTION = "SUN_TAO_999";
    }
}
