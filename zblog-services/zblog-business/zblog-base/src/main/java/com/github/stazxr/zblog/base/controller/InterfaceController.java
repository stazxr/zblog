package com.github.stazxr.zblog.base.controller;

import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.base.domain.dto.query.InterfaceQueryDto;
import com.github.stazxr.zblog.base.domain.vo.InterfaceVo;
import com.github.stazxr.zblog.base.service.InterfaceService;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

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
@Api(value = "InterfaceController", tags = { "接口管理" })
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
    @ApiVersion(BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "分页查询接口列表", code = "INTEQ001")
    public PageInfo<InterfaceVo> pageList(InterfaceQueryDto queryDto) {
        return interfaceService.queryInterfaceListByPage(queryDto);
    }

    /**
     * 导出接口列表
     *
     * @param queryDto 查询参数
     * @param response Response
     */
    @Log
    @GetMapping("/exportInterface")
    @ApiOperation(value = "导出接口列表")
    @ApiVersion(BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "导出接口列表", code = "INTEE001")
    public void exportInterface(InterfaceQueryDto queryDto, HttpServletResponse response) {
        interfaceService.exportInterface(queryDto, response);
    }
}
