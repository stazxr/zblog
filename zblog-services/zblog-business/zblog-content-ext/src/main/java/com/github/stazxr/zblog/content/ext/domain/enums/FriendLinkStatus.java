package com.github.stazxr.zblog.content.ext.domain.enums;

/**
 * 友链审批状态
 *
 * @author SunTao
 * @since 2026-04-18
 */
public enum FriendLinkStatus {
    /**
     * 待审批
     */
    PENDING(0),

    /**
     * 审批通过
     */
    APPROVED(1),

    /**
     * 审批拒绝
     */
    REJECTED(2);

    private final Integer status;

    FriendLinkStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
