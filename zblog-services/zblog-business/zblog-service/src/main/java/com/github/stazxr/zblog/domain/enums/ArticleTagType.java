package com.github.stazxr.zblog.domain.enums;

import lombok.Getter;

/**
 * 文章标签类型
 *
 * @author SunTao
 * @since 2022-11-24
 */
@Getter
public enum ArticleTagType {
    /**
     * 公共
     */
    COMMON(1),

    /**
     * 定制
     */
    CUSTOM(2);

    private final Integer type;

    ArticleTagType(int type) {
        this.type = type;
    }

    public static ArticleTagType of(Integer tagType) {
        for (ArticleTagType item : values()) {
            if (item.type.equals(tagType)) {
                return item;
            }
        }

        return null;
    }
}
