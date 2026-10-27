package com.github.stazxr.zblog.content.domain.enums;

/**
 * 文章定时发布任务状态。
 *
 * @author Sun Tao
 * @since 2026-10-28
 */
public enum ArticleAutoPublishTimingStatus {
    /**
     * 待发布。
     */
    TODO(0, "待发布"),

    /**
     * 执行中。
     */
    DOING(1, "执行中"),

    /**
     * 已发布。
     */
    DONE(2, "已发布"),

    /**
     * 失败。
     */
    FAILED(3, "失败"),

    /**
     * 已取消。
     */
    CANCEL(4, "已取消");

    private final Integer value;

    private final String name;

    ArticleAutoPublishTimingStatus(Integer value, String name) {
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
    public static ArticleAutoPublishTimingStatus of(Integer value) {
        if (value == null) {
            return null;
        }

        for (ArticleAutoPublishTimingStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }

        return null;
    }
}
