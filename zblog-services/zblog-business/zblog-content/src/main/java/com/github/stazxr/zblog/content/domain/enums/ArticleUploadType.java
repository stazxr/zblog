package com.github.stazxr.zblog.content.domain.enums;

/**
 * 文章上传配置
 *
 * @author SunTao
 * @since 2026-11-06
 */
public enum ArticleUploadType {
    ATTACHMENT("attachment"),
    VIDEO("video"),
    IMAGE("image");

    private final String code;

    ArticleUploadType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}