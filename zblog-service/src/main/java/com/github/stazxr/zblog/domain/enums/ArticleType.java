package com.github.stazxr.zblog.domain.enums;

import lombok.Getter;

/**
 * 文章类型
 *
 * @author SunTao
 * @since 2021-07-31
 */
@Getter
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

    public static ArticleType of(Integer articleType) {
        for (ArticleType item : values()) {
            if (item.type.equals(articleType)) {
                return item;
            }
        }

        return null;
    }
}
