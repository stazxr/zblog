package com.github.stazxr.zblog.content.ext.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.content.ext.domain.dto.query.VisitorLogQueryDto;
import com.github.stazxr.zblog.content.ext.domain.vo.VisitorLogVo;
import com.github.stazxr.zblog.content.ext.service.VisitorLogService;
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
 * 访客日志管理
 *
 * @author SunTao
 * @since 2026-07-21
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/visitorLog")
@Api(value = "VisitorLogController", tags = { "访客日志管理" })
public class VisitorLogController {
    private final VisitorLogService visitorLogService;

    /**
     * 分页查询访客日志列表
     *
     * @param queryDto 查询参数
     * @return IPage<VisitorLogVo>
     */
    @GetMapping(value = "/pageList")
    @ApiOperation(value = "分页查询访客日志列表")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "分页查询访客日志列表", code = "VISLQ001")
    public IPage<VisitorLogVo> queryVisitorLogListByPage(VisitorLogQueryDto queryDto) {
        return visitorLogService.queryVisitorLogListByPage(queryDto);
    }

    /**
     * 查询访客日志详情
     *
     * @param visitorLogId 访客日志id
     * @return VisitorLogVo
     */
    @GetMapping(value = "/queryVisitorLogDetail")
    @ApiOperation(value = "查询访客日志详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "visitorLogId", value = "访客日志id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "查询访客日志详情", code = "VISLQ002")
    public VisitorLogVo queryVisitorLogDetail(@RequestParam Long visitorLogId) {
        return visitorLogService.queryVisitorLogDetail(visitorLogId);
    }
}
