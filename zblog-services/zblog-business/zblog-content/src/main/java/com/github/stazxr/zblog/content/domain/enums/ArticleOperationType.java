package com.github.stazxr.zblog.content.domain.enums;

/**
 * 文章操作类型
 *
 * @author Sun Tao
 * @since 2026-10-27
 */
public enum ArticleOperationType {
    /**
     * 初始化
     */
    INIT(1, "初始化"),

    /**
     * 修改
     */
    UPDATE(2, "修改"),

    /**
     * 发布
     */
    PUBLISH(3, "发布"),

    /**
     * 定时发布
     */
    SCHEDULE(4, "定时发布"),

    /**
     * 审核
     */
    REVIEW(5, "审核"),

    /**
     * 移至回收站
     */
    DELETE1(6, "移至回收站"),

    /**
     * 彻底删除
     */
    DELETE2(7, "彻底删除"),

    /**
     * 还原
     */
    RESET(8, "还原");

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
