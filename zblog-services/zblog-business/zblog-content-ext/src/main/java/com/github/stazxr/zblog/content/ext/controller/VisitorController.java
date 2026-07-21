package com.github.stazxr.zblog.content.ext.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.content.ext.domain.dto.query.VisitorQueryDto;
import com.github.stazxr.zblog.content.ext.domain.vo.VisitorVo;
import com.github.stazxr.zblog.content.ext.service.VisitorService;
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
 * 访客管理
 *
 * @author SunTao
 * @since 2026-07-21
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/visitor")
@Api(value = "VisitorController", tags = { "访客管理" })
public class VisitorController {
    private final VisitorService visitorService;

    /**
     * 分页查询访客列表
     *
     * @param queryDto 查询参数
     * @return IPage<VisitorVo>
     */
    @GetMapping(value = "/pageList")
    @ApiOperation(value = "分页查询访客列表")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "分页查询访客列表", code = "VISIQ001")
    public IPage<VisitorVo> queryVisitorListByPage(VisitorQueryDto queryDto) {
        return visitorService.queryVisitorListByPage(queryDto);
    }

    /**
     * 查询访客详情
     *
     * @param visitorId 访客id
     * @return VisitorVo
     */
    @GetMapping(value = "/queryVisitorDetail")
    @ApiOperation(value = "查询访客详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "visitorId", value = "访客id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "查询访客详情", code = "VISIQ002")
    public VisitorVo queryVisitorDetail(@RequestParam String visitorId) {
        return visitorService.queryVisitorDetail(visitorId);
    }
}
