package com.github.stazxr.zblog.content.ext.domain.error;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 网站链接错误码定义。
 *
 * @author SunTao
 * @since 2026-08-23
 */
public enum WebsiteLinkErrorCode implements ErrorCode {
    /** 网站链接类型已存在 */
    EWEBLA001("WEBSITE_LINK_TYPE_EXISTS");

    private final String i18nKey;

    WebsiteLinkErrorCode(String i18nKey) {
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
