package com.github.stazxr.zblog.content.domain.enums;

/**
 * 文章权限
 *
 * @author SunTao
 * @since 2022-06-07
 */
public enum ArticlePerm {
    /**
     * 公开
     */
    OPEN(1),

    /**
     * 私密
     */
    SELF(2),

    /**
     * 密码
     */
    PASSWORD(3);

    private final Integer type;

    ArticlePerm(int type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
