package com.github.stazxr.zblog.content.domain.enums;

/**
 * 文章状态
 *
 * @author SunTao
 * @since 2021-07-31
 */
public enum ArticleStatus {
    /**
     * 草稿
     */
    DRAFT(1),

    /**
     * 待审核（下一个状态为 -> 已发布）
     */
    REVIEW(2),

    /**
     * 待审核（自动发布的待审核，下一个状态为 -> 待发布）
     */
    PUBLISH_REVIEW(3),

    /**
     * 待发布（定时）
     */
    TIME_PUBLISH(4),

    /**
     * 审核不通过
     */
    FAILED_REVIEW(5),

    /**
     * 已发布
     */
    PUBLISHED(6),

    /**
     * 临时下线（已发布的文章由于某些原因临时下线）
     */
    TEMP_DOWN(7),

    /**
     * 待整改（已发布的文章由于某些原因需要内容调整）
     */
    TO_CHANGE(8),

    /**
     * 回收站
     */
    RECYCLE(9),

    /**
     * 已删除
     */
    DELETED(99);

    private final Integer type;

    ArticleStatus(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
