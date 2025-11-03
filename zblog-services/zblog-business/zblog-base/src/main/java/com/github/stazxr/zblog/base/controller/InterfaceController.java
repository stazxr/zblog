package com.github.stazxr.zblog.base.controller;

import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.base.domain.dto.query.InterfaceQueryDto;
import com.github.stazxr.zblog.base.domain.vo.InterfaceVo;
import com.github.stazxr.zblog.base.service.InterfaceService;
import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.base.BaseConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 接口管理
 *
 * @author SunTao
 * @since 2025-11-01
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interfaces")
@Api(value = "InterfaceController", tags = { "接口控制器" })
public class InterfaceController {
    private final InterfaceService interfaceService;

    /**
     * 分页查询接口列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<InterfaceVo>
     */
    @GetMapping(value = "/pageList")
    @ApiOperation(value = "分页查询接口列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_5_0_0 })
    @Router(name = "分页查询接口列表", code = "INTEQ001")
    public PageInfo<InterfaceVo> pageList(InterfaceQueryDto queryDto) {
        return interfaceService.queryInterfaceListByPage(queryDto);
    }
}
