package com.github.stazxr.zblog.domain.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 文章类型
 *
 * @author SunTao
 * @since 2021-07-31
 */
public enum ArticleType implements IEnum<Integer> {
    /**
     * 原创
     */
    ORIGINAL(1, "原创"),

    /**
     * 转载
     */
    REPRINT(2, "转载");

    private final int value;

    private final String name;

    ArticleType(int value, String name) {
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

    public static ArticleType getInstance(int value) {
        ArticleType[] values = ArticleType.values();
        for (ArticleType articleType : values) {
            if (articleType.value == value) {
                return articleType;
            }
        }

        // 找不到就抛出异常
        throw new IllegalStateException("非法的参数[" + value + "]");
    }
}
