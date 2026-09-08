package com.github.stazxr.zblog.portal.domain.enums;

/**
 * 主题类型
 *
 * @author SunTao
 * @since 2026-06-14
 */
public enum PortalUserType {
    /**
     * 登录用户
     */
    USER(1),

    /**
     * 访客
     */
    VISITOR(2);

    private final Integer type;

    PortalUserType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
