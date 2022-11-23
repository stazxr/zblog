package com.github.stazxr.zblog.domain.enums;

import lombok.Getter;

/**
 * 文章权限
 *
 * @author SunTao
 * @since 2022-06-07
 */
@Getter
public enum ArticlePerm {
    /**
     * 公开
     */
    OPEN_SHOW(1),

    /**
     * 登录可见
     */
    LOGIN_SHOW(2),

    /**
     * 仅自己可见，无需审核
     */
    SELF_SHOW(3);

    private final Integer type;

    ArticlePerm(int type) {
        this.type = type;
    }

    public static ArticlePerm of(Integer articlePerm) {
        for (ArticlePerm item : values()) {
            if (item.type.equals(articlePerm)) {
                return item;
            }
        }

        return null;
    }
}
