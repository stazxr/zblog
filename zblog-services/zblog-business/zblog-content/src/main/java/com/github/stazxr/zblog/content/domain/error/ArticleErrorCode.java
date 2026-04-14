package com.github.stazxr.zblog.content.domain.error;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 文章错误码定义。
 *
 * @author SunTao
 * @since 2026-04-08
 */
public enum ArticleErrorCode implements ErrorCode {
    /**  */
    EARTIA001("ARTICLE_QUERY_TAG_STATUS_EMPTY");

    private final String i18nKey;

    ArticleErrorCode(String i18nKey) {
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
