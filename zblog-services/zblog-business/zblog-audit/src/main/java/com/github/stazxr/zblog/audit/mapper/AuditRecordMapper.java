package com.github.stazxr.zblog.audit.mapper;

import com.github.stazxr.zblog.audit.model.AuditRecord;
import com.github.stazxr.zblog.audit.model.AuditResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 根据 OID 查询审核记录
     * @param oid OID
     * @return AuditResult
     */
    AuditRecord selectByOid(@Param("oid") Long oid);
}
