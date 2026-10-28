package com.github.stazxr.zblog.content.domain.enums;

/**
 * 文章操作类型
 *
 * @author Sun Tao
 * @since 2026-10-27
 */
public enum ArticleOperationType {
    /**
     * 创建文章
     */
    CREATE(1, "创建文章"),

    /**
     * 保存文章
     */
    SAVE(2, "保存文章"),

    /**
     * 提交审核
     */
    REVIEW(3, "提交审核"),

    /**
     * 发布文章
     */
    PUBLISH(4, "发布文章"),

    /**
     * 定时发布
     */
    SCHEDULE(5, "定时发布"),

    /**
     * 审核拒绝
     */
    REJECT(6, "审核拒绝"),

    /**
     * 移至回收站
     */
    DELETE1(7, "移至回收站"),

    /**
     * 删除文章
     */
    DELETE2(8, "删除文章"),

    /**
     * 清空回收站
     */
    DELETE3(9, "清空回收站"),

    /**
     * 恢复文章
     */
    RESET(10, "恢复文章");

    private final Integer value;

    private final String name;

    ArticleOperationType(Integer value, String name) {
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
