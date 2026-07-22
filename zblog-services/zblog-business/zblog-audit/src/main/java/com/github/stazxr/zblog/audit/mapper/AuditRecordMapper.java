package com.github.stazxr.zblog.audit.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.audit.model.AuditRecord;
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
     * 根据 ID 查询审核记录
     *
     * @param id ID
     * @return AuditResult
     */
    AuditRecord selectById(@Param("id") String id);

    /**
     * 分页查询自动审核记录列表
     *
     * @param page 分页参数
     * @param scene 查询参数
     * @param decision 查询参数
     * @return IPage<AuditRecord>
     */
    IPage<AuditRecord> selectAutoAuditRecordList(@Param("page") Page<AuditRecord> page, @Param("scene") String scene, @Param("decision") String decision);

    /**
     * 插入审核记录
     *
     * @param record 审核记录
     * @return 插入行数
     */
    int insert(AuditRecord record);

    /**
     * 根据 OID 查询审核记录
     *
     * @param oid OID
     * @return AuditResult
     */
    AuditRecord selectByOid(@Param("oid") Long oid);
}
