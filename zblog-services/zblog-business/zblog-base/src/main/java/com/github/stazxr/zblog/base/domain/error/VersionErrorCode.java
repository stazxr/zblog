package com.github.stazxr.zblog.base.domain.error;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 版本错误码定义。
 *
 * @author SunTao
 * @since 2026-02-07
 */
public enum VersionErrorCode implements ErrorCode {
    /** 版本已存在 */
    EVERSA000("VERSION_VERSIONNAME_EXISTS");

    private final String i18nKey;

    VersionErrorCode(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    /**
     * 错误码，唯一且符合规范
     *
     * @return 错误码字符串
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
