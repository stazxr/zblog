package com.github.stazxr.zblog.content.domain.enums;

/**
 * 文章状态。
 *
 * @author Sun Tao
 * @since 2026-10-18
 */
public enum ArticleStatus {
    /**
     * 草稿。
     */
    DRAFT(1, "草稿"),

    /**
     * 待审核。
     */
    PENDING_REVIEW(2, "待审核"),

    /**
     * 待发布，已审核通过，等待计划发布时间。
     */
    SCHEDULED(3, "待发布"),

    /**
     * 审核未通过。
     */
    REVIEW_REJECTED(4, "审核未通过"),

    /**
     * 已发布。
     */
    PUBLISHED(5, "已发布"),

    /**
     * 已下线。
     */
    OFFLINE(91, "已下线");

    private final Integer value;

    private final String name;

    ArticleStatus(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    /**
     * 根据状态值获取枚举。
     *
     * @param value 状态值
     * @return 对应枚举，不存在或参数为空时返回 null
     */
    public static ArticleStatus of(Integer value) {
        if (value == null) {
            return null;
        }

        for (ArticleStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }

        return null;
    }
}
