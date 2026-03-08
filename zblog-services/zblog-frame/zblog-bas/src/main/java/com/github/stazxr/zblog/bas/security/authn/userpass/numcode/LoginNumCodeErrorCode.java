package com.github.stazxr.zblog.bas.security.authn.userpass.numcode;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 登录验证码错误码定义。
 *
 * @author SunTao
 * @since 2026-03-09
 */
public enum LoginNumCodeErrorCode implements ErrorCode {
    /** 验证码异常 */
    ELOGNC000("error.service.logincode.exception"),
    /** 请输入验证码 */
    ELOGNC001("error.service.logincode.required"),
    /** 验证码已过期 */
    ELOGNC002("error.service.logincode.expired"),
    /** 验证码错误 */
    ELOGNC003("error.service.logincode.incorrect");

    private final String i18nKey;

    LoginNumCodeErrorCode(String i18nKey) {
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
