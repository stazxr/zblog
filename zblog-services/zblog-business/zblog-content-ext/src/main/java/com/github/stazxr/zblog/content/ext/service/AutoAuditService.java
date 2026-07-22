package com.github.stazxr.zblog.content.ext.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.audit.model.AuditRecord;
import com.github.stazxr.zblog.content.ext.domain.dto.query.AuditRecordQueryDto;

/**
 * 自动审核管理业务层
 *
 * @author SunTao
 * @since 2027-07-23
 */
public interface AutoAuditService {
    /**
     * 分页查询自动审核记录列表
     *
     * @param queryDto 查询参数
     * @return IPage<AuditRecord>
     */
    IPage<AuditRecord> queryAutoAuditRecordsByPage(AuditRecordQueryDto queryDto);

    /**
     * 查询自动审核记录详情
     *
     * @param auditRecordId 自动审核记录id
     * @return AuditRecord
     */
    AuditRecord queryAutoAuditRecordDetail(String auditRecordId);
}
