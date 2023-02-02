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
     * 全部可见：所有用户将均可访问和阅读
     */
    OPEN_SHOW(1),

    /**
     * 登录可见：仅系统用户可访问和阅读
     */
    @Deprecated
    LOGIN_SHOW(2),

    /**
     * 仅我可见：仅您自己可访问和阅读
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
