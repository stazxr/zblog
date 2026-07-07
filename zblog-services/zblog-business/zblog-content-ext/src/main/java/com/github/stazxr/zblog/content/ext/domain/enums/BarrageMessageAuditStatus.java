package com.github.stazxr.zblog.content.ext.domain.enums;

/**
 * 弹幕审批状态
 *
 * @author SunTao
 * @since 2026-07-07
 */
public enum BarrageMessageAuditStatus {
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
    REJECTED(2),

    /**
     * 待人为确认
     */
    MANUAL(3);

    private final Integer status;

    BarrageMessageAuditStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
