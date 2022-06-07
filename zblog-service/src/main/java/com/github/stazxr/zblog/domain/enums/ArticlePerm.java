package com.github.stazxr.zblog.domain.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 文章权限
 *
 * @author SunTao
 * @since 2022-06-07
 */
public enum ArticlePerm implements IEnum<Integer> {
    /**
     * 公开
     */
    OPEN_SHOW(1, "公开"),

    /**
     * 登录可见
     */
    LOGIN_SHOW(2, "登录可见"),

    /**
     * 仅自己可见
     */
    SELF_SHOW(3, "仅自己可见");

    private final int value;

    private final String name;

    ArticlePerm(int value, String name) {
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

    public static ArticlePerm getInstance(int value) {
        ArticlePerm[] values = ArticlePerm.values();
        for (ArticlePerm articlePerm : values) {
            if (articlePerm.value == value) {
                return articlePerm;
            }
        }

        // 找不到就抛出异常
        throw new IllegalStateException("非法的参数[" + value + "]");
    }
}
