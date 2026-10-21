package com.github.stazxr.zblog.content.domain.error;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 文章错误码定义。
 *
 * @author SunTao
 * @since 2026-04-08
 */
public enum ArticleErrorCode implements ErrorCode {
    /** 文章状态标签不能为空 */
    EARTIA001("ARTICLE_QUERY_TAG_STATUS_EMPTY"),
    /** 封面自动生成失败，请选择其他封面类型 */
    EARTIA002("ARTICLE_TITLE_GENERATE_ERROR"),
    /** 请上传文章封面 */
    EARTIA003("ARTICLE_COVER_IMAGE_REQUIRED"),
    /** 请填写原文地址 */
    EARTIA004("ARTICLE_SOURCE_URL_REQUIRED"),
    /** 没有查看权限 */
    EARTIA005("ARTICLE_READ_NO_PERMISSION"),
    /** 没有编辑权限 */
    EARTIA006("ARTICLE_EDIT_NO_PERMISSION"),
    /** 没有删除权限 */
    EARTIA007("ARTICLE_DELETE_NO_PERMISSION"),
    /** 文章封面超出最大上传数 */
    EARTIA008("ARTICLE_COVER_IMAGE_OVER_MAX"),
    /** 文章状态异常 */
    EARTIA009("ARTICLE_X_STATUS_INVALID");

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
