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
    DEFAULT(0, "默认封面"),

    /**
     * 单封面
     */
    SINGLE(1, "单封面"),

    /**
     * 多封面
     */
    MULTIPLE(2, "多封面"),

    /**
     * 随机封面
     */
    RANDOM(3, "随机封面"),

    /**
     * 标题生成
     */
    TITLE(4, "标题生成"),

    /**
     * 无封面
     */
    NONE(5, "无封面");

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
