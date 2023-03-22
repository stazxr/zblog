package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.domain.dto.DictDto;
import com.github.stazxr.zblog.base.domain.dto.RouterDto;
import com.github.stazxr.zblog.base.domain.dto.query.RouterQueryDto;
import com.github.stazxr.zblog.base.domain.vo.RouterVo;
import com.github.stazxr.zblog.base.service.RouterService;
import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.util.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 系统路由管理
 *
 * @author SunTao
 * @since 2020-12-01
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/router")
@Api(value = "RouterController", tags = { "路由控制器" })
public class RouterController {
    private final RouterService routerService;

    /**
     * 分页查询路由列表
     *
     * @param queryDto 查询参数
     * @return routerList
     */
    @GetMapping("pageList")
    @ApiOperation("分页查询路由列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询路由列表", code = "queryRouterListByPage")
    public Result pageList(RouterQueryDto queryDto) {
        return Result.success().data(routerService.queryRouterListByPage(queryDto));
    }

    /**
     * 分页查询黑白名单列表
     *
     * @param queryDto 查询参数
     * @return blackOrWhiteList
     */
    @GetMapping("pageBlackOrWhiteList")
    @ApiOperation("分页查询黑白名单列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询黑白名单列表", code = "pageBlackOrWhiteList")
    public Result pageBlackOrWhiteList(RouterQueryDto queryDto) {
        return Result.success().data(routerService.pageBlackOrWhiteList(queryDto));
    }

    /**
     * 根据权限编码查询路由信息
     *
     * @param code 权限编码
     * @return routerVo
     */
    @GetMapping(value = "/queryRouterByCode")
    @ApiOperation("根据权限编码查询路由信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "code", value = "权限编码", required = true, dataTypeClass = String.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "根据权限编码查询路由信息", code = "queryRouterByCode", level = BaseConst.PermLevel.PUBLIC)
    public Result queryRouterByCode(@RequestParam String code) {
        RouterVo routerVo = routerService.queryRouterByCode(code);
        Assert.notNull(routerVo, "根据权限编码查询路由信息失败，数据不存在");
        return Result.success().data(routerVo);
    }

    /**
     * 更新接口的日志展示状态
     *
     * @param routerDto 路由信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/updateLogShowStatus")
    @ApiOperation("更新接口的日志展示状态")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "更新接口的日志展示状态", code = "updateLogShowStatus")
    public Result updateLogShowStatus(@RequestBody RouterDto routerDto) {
        routerService.updateLogShowStatus(routerDto);
        return Result.success();
    }

    /**
     * 新增黑白名单
     *
     * @param dictDto 字典信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/addBlackOrWhiteRouter")
    @ApiOperation("新增黑白名单")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "新增黑白名单", code = "addBlackOrWhiteRouter")
    public Result addBlackOrWhiteRouter(@RequestBody DictDto dictDto) {
        routerService.addBlackOrWhiteRouter(dictDto);
        return Result.success();
    }

    /**
     * 编辑黑白名单
     *
     * @param dictDto 字典信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/editBlackOrWhiteRouter")
    @ApiOperation("编辑黑白名单")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "编辑黑白名单", code = "editBlackOrWhiteRouter")
    public Result editBlackOrWhiteRouter(@RequestBody DictDto dictDto) {
        routerService.editBlackOrWhiteRouter(dictDto);
        return Result.success();
    }

    /**
     * 修改黑白名单状态
     *
     * @param dictDto 字典信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/changeBlackOrWhiteRouterStatus")
    @ApiOperation("修改黑白名单状态")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "修改黑白名单状态", code = "changeBlackOrWhiteRouterStatus")
    public Result changeBlackOrWhiteRouterStatus(@RequestBody DictDto dictDto) {
        routerService.changeBlackOrWhiteRouterStatus(dictDto);
        return Result.success();
    }

    /**
     * 删除黑白名单
     *
     * @param dictId 字典序列
     * @return Result
     */
    @Log
    @PostMapping(value = "/deleteBlackOrWhiteRouter")
    @ApiOperation("删除黑白名单")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "dictId", value = "字典id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "删除黑白名单", code = "deleteBlackOrWhiteRouter")
    public Result deleteBlackOrWhiteRouter(@RequestParam Long dictId) {
        routerService.deleteBlackOrWhiteRouter(dictId);
        return Result.success();
    }
}
