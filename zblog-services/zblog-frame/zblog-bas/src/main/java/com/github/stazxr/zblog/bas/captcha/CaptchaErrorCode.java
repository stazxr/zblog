package com.github.stazxr.zblog.bas.captcha;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 验证码错误码定义。
 *
 * @author SunTao
 * @since 2026-01-26
 */
public enum CaptchaErrorCode implements ErrorCode {
    /**
     * 验证码创建失败
     */
    SCAPTA000("error.system.captcha.failed"),
    ;

    private final String i18nKey;

    CaptchaErrorCode(String i18nKey) {
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