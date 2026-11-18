package com.github.stazxr.zblog.content.domain.enums;

/**
 * 文章审批状态
 *
 * @author SunTao
 * @since 2026-11-19
 */
public enum ArticleAuditStatus {
    /**
     * 审批通过
     */
    APPROVED(1),

    /**
     * 审批拒绝
     */
    REJECTED(2);

    private final Integer status;

    ArticleAuditStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
