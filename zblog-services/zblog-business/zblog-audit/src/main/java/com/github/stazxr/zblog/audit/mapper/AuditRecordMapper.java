package com.github.stazxr.zblog.audit.mapper;

import com.github.stazxr.zblog.audit.model.AuditRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 审核记录Mapper
 *
 * @author Sun Tao
 * @since 2026-07-04
 */
@Mapper
public interface AuditRecordMapper {
    /**
     * 插入审核记录
     */
    int insert(AuditRecord record);
}
