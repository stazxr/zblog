package com.github.stazxr.zblog.domain.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 文章状态
 *
 * @author SunTao
 * @since 2021-07-31
 */
public enum ArticleStatus implements IEnum<Integer> {
    /**
     * 草稿
     */
    DRAFT(1, "草稿"),

    /**
     * 审核中
     */
    REVIEW(2, "审核中"),

    /**
     * 修改审核中（已发布的文章不允许修改，修改需要重新审核）
     */
    UPDATE_REVIEW(3, "修改审核中"),

    /**
     * 审核拒绝
     */
    FAILED_REVIEW(4, "审核拒绝"),

    /**
     * 已发布
     */
    PUBLISHED(5, "已发布"),

    /**
     * 临时下线（已发布的文章由于某些原因临时下线）
     */
    TEMP_DOWN(6, "临时下线"),

    /**
     * 待整改（已发布的文章由于某些原因需要重新修改）
     */
    TO_CHANGE(7, "待整改"),

    /**
     * 回收站
     */
    RECYCLE(8, "回收站"),

    /**
     * 已删除
     */
    DELETED(9, "已删除");

    private final int value;

    private final String name;

    ArticleStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * 枚举数据库存储值
     */
    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static ArticleStatus getInstance(int value) {
        ArticleStatus[] values = ArticleStatus.values();
        for (ArticleStatus articleStatus : values) {
            if (articleStatus.value == value) {
                return articleStatus;
            }
        }

        // 找不到就抛出异常
        throw new IllegalStateException("非法的参数[" + value + "]");
    }
}
