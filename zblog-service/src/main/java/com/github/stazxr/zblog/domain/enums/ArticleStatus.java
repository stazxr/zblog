package com.github.stazxr.zblog.domain.enums;

import lombok.Getter;

/**
 * 文章状态
 *
 * @author SunTao
 * @since 2021-07-31
 */
@Getter
public enum ArticleStatus {
    /**
     * 草稿
     */
    DRAFT(1),

    /**
     * 审核中
     */
    REVIEW(2),

    /**
     * 修改审核中（已发布的文章如果被修改，需要重新审核）
     */
    UPDATE_REVIEW(3),

    /**
     * 审核拒绝
     */
    FAILED_REVIEW(4),

    /**
     * 已发布
     */
    PUBLISHED(5),

    /**
     * 临时下线（已发布的文章由于某些原因临时下线）
     */
    TEMP_DOWN(6),

    /**
     * 待整改（已发布的文章由于某些原因需要内容调整）
     */
    TO_CHANGE(7),

    /**
     * 回收站
     */
    RECYCLE(8),

    /**
     * 已删除
     */
    DELETED(9);

    private final Integer type;

    ArticleStatus(Integer type) {
        this.type = type;
    }

    public static ArticleStatus of(Integer articleStatus) {
        for (ArticleStatus item : values()) {
            if (item.type.equals(articleStatus)) {
                return item;
            }
        }

        return null;
    }
}
