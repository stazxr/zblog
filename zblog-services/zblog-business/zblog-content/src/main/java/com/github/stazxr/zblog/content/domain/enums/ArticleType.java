package com.github.stazxr.zblog.content.domain.enums;

/**
 * 文章类型
 *
 * @author SunTao
 * @since 2021-07-31
 */
public enum ArticleType {
    /**
     * 原创
     */
    ORIGINAL(1),

    /**
     * 转载
     */
    REPRINT(2),

    /**
     * 翻译
     */
    TRANSLATE(3);

    private final Integer type;

    ArticleType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
