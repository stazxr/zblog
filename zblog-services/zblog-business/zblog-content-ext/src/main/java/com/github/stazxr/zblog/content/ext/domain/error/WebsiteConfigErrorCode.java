package com.github.stazxr.zblog.content.ext.domain.error;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 网站配置错误码定义。
 *
 * @author SunTao
 * @since 2026-09-08
 */
public enum WebsiteConfigErrorCode implements ErrorCode {
    /** 网站作者不存在 */
    EWEBCA001("WEBSITE_CONFIG_AUTHOR_NOT_EXISTS");

    private final String i18nKey;

    WebsiteConfigErrorCode(String i18nKey) {
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
