package com.github.stazxr.zblog.bas.security.exception;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 认证失败错误码定义。
 *
 * @author SunTao
 * @since 2026-03-16
 */
public enum AuthenticationErrorCode implements ErrorCode {
    /** 用户名或密码错误 */
    EAUTHN000("error.service.authentication.userOrPwdError"),
    /** 用户已锁定 */
    EAUTHN001("error.service.authentication.locked"),
    /** 用户已禁用 */
    EAUTHN002("error.service.authentication.disabled"),
    /** 账号已过期 */
    EAUTHN003("error.service.authentication.userExpired"),
    /** 密码已过期 */
    EAUTHN004("error.service.authentication.passwordExpired"),
    /** 系统用户禁止登录 */
    EAUTHN005("error.service.authentication.systemDenied");

    private final String i18nKey;

    AuthenticationErrorCode(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    /**
     * 错误码
     */
    @Override
    public String getCode() {
        return name();
    }

    /**
     * 国际化 key，用于获取对应消息
     *
     * @return i18n key
     */
    @Override
    public String getI18nKey() {
        return i18nKey;
    }
}
