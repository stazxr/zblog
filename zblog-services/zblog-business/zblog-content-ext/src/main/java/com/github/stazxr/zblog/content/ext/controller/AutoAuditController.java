package com.github.stazxr.zblog.content.ext.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.audit.model.AuditRecord;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.content.ext.domain.dto.query.AuditRecordQueryDto;
import com.github.stazxr.zblog.content.ext.service.AutoAuditService;
import com.github.stazxr.zblog.core.base.BaseConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自动审核管理
 *
 * @author SunTao
 * @since 2026-07-23
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/autoAudits")
@Api(value = "AutoAuditController", tags = "自动审核管理")
public class AutoAuditController {
    private final AutoAuditService autoAuditService;

    /**
     * 分页查询自动审核记录列表
     *
     * @param queryDto 查询参数
     * @return IPage<AuditRecord>
     */
    @GetMapping(value = "/pageList")
    @ApiOperation(value = "分页查询自动审核记录列表")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "分页查询自动审核记录列表", code = "AUDTQ001")
    public IPage<AuditRecord> queryAutoAuditRecordsByPage(AuditRecordQueryDto queryDto) {
        return autoAuditService.queryAutoAuditRecordsByPage(queryDto);
    }

    /**
     * 查询自动审核记录详情
     *
     * @param auditRecordId 自动审核记录id
     * @return AuditRecord
     */
    @GetMapping(value = "/queryAutoAuditRecordDetail")
    @ApiOperation(value = "查询自动审核记录详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "auditRecordId", value = "自动审核记录id", required = true, dataTypeClass = String.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "查询自动审核记录详情", code = "AUDTQ002")
    public AuditRecord queryAutoAuditRecordDetail(@RequestParam String auditRecordId) {
        return autoAuditService.queryAutoAuditRecordDetail(auditRecordId);
    }
}
