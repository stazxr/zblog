package com.github.stazxr.zblog.bas.encryption;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 加解密错误码定义。
 *
 * @author SunTao
 * @since 2026-01-25
 */
public enum EncryptionErrorCode implements ErrorCode {
    /**
     * 加解密失败
     */
    SENCYA000("error.system.encryption");

    private final String i18nKey;

    EncryptionErrorCode(String i18nKey) {
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
