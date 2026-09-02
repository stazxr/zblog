package com.github.stazxr.zblog.content.ext.domain.enums;

/**
 * 评论类型
 *
 * @author SunTao
 * @since 2026-08-31
 */
public enum CommentType {
    /**
     * 文章
     */
    ARTICLE(1, "文章"),

    /**
     * 留言
     */
    MESSAGE(2, "留言");

    private final Integer value;

    private final String name;

    CommentType(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
