package com.github.stazxr.zblog.domain.error;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 分类错误码定义。
 *
 * @author SunTao
 * @since 2026-03-27
 */
public enum CategoryErrorCode implements ErrorCode {
    /** 分类名称已存在 */
    ECATEA000("CATEGORY_NAME_EXISTS"),
    /** 分类唯一标识已存在 */
    ECATEA001("CATEGORY_SLUG_EXISTS"),
    /** 存在子节点，无法被删除 */
    ECATEA002("CATEGORY_DELETE_WITH_CHILDREN"),
    /** 分类已与文章关联 */
    ECATEA003("CATEGORY_DELETE_WITH_ARTICLE");

    private final String i18nKey;

    CategoryErrorCode(String i18nKey) {
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
