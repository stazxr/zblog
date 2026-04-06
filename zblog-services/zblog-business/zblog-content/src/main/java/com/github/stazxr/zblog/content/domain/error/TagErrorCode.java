package com.github.stazxr.zblog.content.domain.error;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 标签错误码定义。
 *
 * @author SunTao
 * @since 2026-04-06
 */
public enum TagErrorCode implements ErrorCode {
    /** 标签名称已存在 */
    ETAGSA000("TAG_NAME_EXISTS"),
    /** 标签唯一标识已存在 */
    ETAGSA001("TAG_SLUG_EXISTS"),
    /** 标签已与文章关联 */
    ETAGSA002("TAG_DELETE_WITH_ARTICLE");

    private final String i18nKey;

    TagErrorCode(String i18nKey) {
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
