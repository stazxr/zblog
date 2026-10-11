package com.github.stazxr.zblog.content.domain.enums;

/**
 * 文章封面类型
 *
 * @author SunTao
 * @since 2026-09-18
 */
public enum ArticleCoverImageType {
    /**
     * 默认封面
     */
    DEFAULT(1, "默认封面"),

    /**
     * 单封面
     */
    SINGLE(2, "单封面"),

    /**
     * 多封面
     */
    MULTIPLE(3, "多封面"),

    /**
     * 随机封面
     */
    RANDOM(4, "随机封面"),

    /**
     * 标题生成
     */
    TITLE(5, "标题生成"),

    /**
     * 无封面
     */
    NONE(6, "无封面");

    private final Integer value;

    private final String name;

    ArticleCoverImageType(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static ArticleCoverImageType of(Integer status) {
        for (ArticleCoverImageType value : ArticleCoverImageType.values()) {
            if (value.getValue().equals(status)) {
                return value;
            }
        }
        return null;
    }
}
