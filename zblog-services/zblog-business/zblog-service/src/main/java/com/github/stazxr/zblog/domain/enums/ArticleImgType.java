package com.github.stazxr.zblog.domain.enums;

import lombok.Getter;

/**
 * 文章标签类型
 *
 * @author SunTao
 * @since 2022-11-24
 */
@Getter
public enum ArticleImgType {
    /**
     * 单封面
     */
    SINGLE(1),

    /**
     * 多封面
     */
    MULTI(2),

    /**
     * 默认封面
     */
    DEFAULT(3),

    /**
     * 自动生成封面
     */
    AUTO_GENERATE(4),

    /**
     * 无封面
     */
    NONE(5);

    private final Integer type;

    ArticleImgType(int type) {
        this.type = type;
    }

    public static ArticleImgType of(Integer imgType) {
        for (ArticleImgType item : values()) {
            if (item.type.equals(imgType)) {
                return item;
            }
        }

        return null;
    }
}
