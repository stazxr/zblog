package com.github.stazxr.zblog.content.domain.enums;

/**
 * 文章微信公众号同步状态。
 *
 * @author Sun Tao
 * @since 2026-11-01
 */
public enum ArticleWechatSyncStatus {
    /**
     * 待同步。
     */
    TODO(0, "待同步"),

    /**
     * 同步成功。
     */
    SUCCESS(1, "同步成功"),

    /**
     * 同步失败。
     */
    FAILED(2, "同步失败");

    private final Integer value;

    private final String name;

    ArticleWechatSyncStatus(Integer value, String name) {
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
