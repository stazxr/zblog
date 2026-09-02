package com.github.stazxr.zblog.content.ext.domain.enums;

/**
 * 评论状态
 *
 * @author SunTao
 * @since 2026-08-31
 */
public enum CommentStatus {
    /**
     * 待审核
     */
    PENDING(0, "待审核"),

    /**
     * 正常
     */
    NORMAL(1, "正常"),

    /**
     * 拒绝
     */
    REJECTED(2, "拒绝"),

    /**
     * 删除
     */
    DELETED(3, "删除"),

    /**
     * 待复核
     */
    MANUAL(4, "待复核");

    private final Integer value;

    private final String name;

    CommentStatus(Integer value, String name) {
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