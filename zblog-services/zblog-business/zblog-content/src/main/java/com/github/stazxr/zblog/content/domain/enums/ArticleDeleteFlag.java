package com.github.stazxr.zblog.content.domain.enums;

/**
 * 文章删除状态
 *
 * @author SunTao
 * @since 2026-10-14
 */
public enum ArticleDeleteFlag {
    /**
     * 正常
     */
    DEFAULT(0, "正常"),

    /**
     * 回收站
     */
    RECYCLE(1, "回收站"),

    /**
     * 彻底删除
     */
    DELETED(2, "彻底删除");

    private final Integer value;

    private final String name;

    ArticleDeleteFlag(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static ArticleDeleteFlag of(Integer status) {
        for (ArticleDeleteFlag value : ArticleDeleteFlag.values()) {
            if (value.getValue().equals(status)) {
                return value;
            }
        }
        return null;
    }
}
