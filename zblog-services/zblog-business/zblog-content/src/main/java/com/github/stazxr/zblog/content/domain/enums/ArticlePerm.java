package com.github.stazxr.zblog.content.domain.enums;

/**
 * 文章访问权限类型
 *
 * @author Sun Tao
 * @since 2026-09-19
 */
public enum ArticlePerm {
    /**
     * 公开访问
     */
    PUBLIC(1, "公开访问"),

    /**
     * 登录可见
     */
    LOGIN(2, "登录可见"),

    /**
     * 仅自己可见
     */
    SELF(3, "仅自己可见"),

    /**
     * 公众号验证
     */
    WECHAT(4, "公众号验证"),

    /**
     * 密码访问
     */
    PASSWORD(5, "密码访问"),

    /**
     * 付费访问
     */
    PAY(6, "付费访问"),

    /**
     * 指定用户
     */
    USER(7, "指定用户");

    private final int value;

    private final String name;

    ArticlePerm(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static ArticlePerm of(Integer value) {
        if (value == null) {
            return null;
        }

        for (ArticlePerm item : values()) {
            if (item.value == value) {
                return item;
            }
        }

        return null;
    }
}
