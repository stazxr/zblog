package com.github.stazxr.zblog.content.ext.domain.enums;

/**
 * 友链审批类型
 *
 * @author SunTao
 * @since 2026-08-26
 */
public enum FriendLinkType {
    /**
     * 开源伙伴
     */
    OPEN(1),

    /**
     * 特别推荐
     */
    FEATURED(2),

    /**
     * 同行友站
     */
    NORMAL(3);

    private final Integer type;

    FriendLinkType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
