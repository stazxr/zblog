package com.github.stazxr.zblog.content.ext.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.content.ext.domain.dto.BarrageMessageAuditDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.BarrageMessageQueryDto;
import com.github.stazxr.zblog.content.ext.domain.vo.BarrageMessageVo;
import com.github.stazxr.zblog.content.ext.service.BarrageMessageService;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 弹幕管理
 *
 * @author SunTao
 * @since 2027-07-06
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/barrageMessages")
@Api(value = "BarrageMessageController", tags = {"弹幕管理"})
public class BarrageMessageController {
    private final BarrageMessageService barrageMessageService;

    /**
     * 分页查询弹幕列表
     *
     * @param queryDto 查询参数
     * @return IPage<BarrageMessageVo>
     */
    @GetMapping(value = "/pageList")
    @ApiOperation(value = "分页查询弹幕列表")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "分页查询弹幕列表", code = "BMESQ001")
    public IPage<BarrageMessageVo> queryBarrageMessageListByPage(BarrageMessageQueryDto queryDto) {
        return barrageMessageService.queryBarrageMessageListByPage(queryDto);
    }

    /**
     * 查询弹幕详情
     *
     * @param barrageMessageId 弹幕id
     * @return BarrageMessageVo
     */
    @GetMapping(value = "/queryBarrageMessageDetail")
    @ApiOperation(value = "查询弹幕详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "barrageMessageId", value = "弹幕id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "查询弹幕详情", code = "BMESQ002")
    public BarrageMessageVo queryBarrageMessageDetail(@RequestParam Long barrageMessageId) {
        return barrageMessageService.queryBarrageMessageDetail(barrageMessageId);
    }

    /**
     * 审核弹幕
     *
     * @param auditDto 弹幕审核信息
     */
    @Log
    @PostMapping(value = "/auditBarrageMessage")
    @ApiOperation(value = "审核弹幕")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "审核弹幕", code = "BMESU001")
    public void auditBarrageMessage(@RequestBody @Validated BarrageMessageAuditDto auditDto) {
        barrageMessageService.auditBarrageMessage(auditDto);
    }

    /**
     * 删除弹幕
     *
     * @param barrageMessageId 弹幕id
     */
    @Log
    @PostMapping(value = "/deleteBarrageMessage")
    @ApiOperation(value = "删除弹幕")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "barrageMessageId", value = "弹幕id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "删除弹幕", code = "BMESD001")
    public void deleteBarrageMessage(@RequestParam Long barrageMessageId) {
        barrageMessageService.deleteBarrageMessage(barrageMessageId);
    }
}
