package com.github.stazxr.zblog.bas.notify.mail;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 邮箱错误码定义。
 *
 * @author SunTao
 * @since 2026-01-25
 */
public enum MailErrorCode implements ErrorCode {
    /**
     * 邮箱服务器故障
     */
    SMAILA000("error.system.mail.failed"),
    ;

    private final String i18nKey;

    MailErrorCode(String i18nKey) {
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