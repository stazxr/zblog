package com.github.stazxr.zblog.content.ext.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.audit.mapper.AuditRecordMapper;
import com.github.stazxr.zblog.audit.model.AuditRecord;
import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.content.ext.domain.dto.query.AuditRecordQueryDto;
import com.github.stazxr.zblog.content.ext.service.AutoAuditService;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 自动审核管理业务实现层
 *
 * @author SunTao
 * @since 2027-07-23
 */
@Service
@RequiredArgsConstructor
public class AutoAuditServiceImpl implements AutoAuditService {
    private final AuditRecordMapper auditRecordMapper;

    /**
     * 分页查询自动审核记录列表
     *
     * @param queryDto 查询参数
     * @return IPage<AuditRecord>
     */
    @Override
    public IPage<AuditRecord> queryAutoAuditRecordsByPage(AuditRecordQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();

        // 分页查询
        Page<AuditRecord> page = new Page<>(queryDto.getPage(), queryDto.getPageSize());
        return auditRecordMapper.selectAutoAuditRecordList(page, queryDto.getScene(), queryDto.getDecision());
    }

    /**
     * 查询自动审核记录详情
     *
     * @param auditRecordId 自动审核记录id
     * @return AuditRecord
     */
    @Override
    public AuditRecord queryAutoAuditRecordDetail(String auditRecordId) {
        AuditRecord auditRecord = auditRecordMapper.selectById(auditRecordId);
        return ThrowUtils.requireNonNull(auditRecord, BaseErrorCode.ECOREA001);
    }
}
