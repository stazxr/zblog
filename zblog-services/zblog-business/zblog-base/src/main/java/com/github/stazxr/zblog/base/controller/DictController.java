package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.domain.dto.DictDto;
import com.github.stazxr.zblog.base.domain.dto.query.DictQueryDto;
import com.github.stazxr.zblog.base.service.DictService;
import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 字典管理
 *
 * @author SunTao
 * @since 2021-02-20
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dict")
@Api(value = "DictController", tags = { "字典控制器" })
public class DictController {
    private final DictService dictService;

    /**
     * 分页查询字典列表
     *
     * @param queryDto 查询参数
     * @return dictList
     */
    @GetMapping(value = "/pageList")
    @ApiOperation(value = "分页查询字典列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询字典列表", code = "queryDictListByPage")
    public Result queryDictListByPage(DictQueryDto queryDto) {
        return Result.success().data(dictService.queryDictListByPage(queryDto));
    }

    /**
     * 查询字典子列表
     *
     * @param pid PID
     * @return dictList
     */
    @GetMapping(value = "/queryChildList")
    @ApiOperation(value = "查询字典子列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pid", value = "字典pid", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询字典子列表", code = "queryDictChildList", level = BaseConst.PermLevel.PUBLIC)
    public Result queryChildList(@RequestParam Long pid) {
        return Result.success().data(dictService.queryChildList(pid));
    }

    /**
     * 查询字典详情
     *
     * @param dictId 字典序列
     * @return DictVo
     */
    @GetMapping(value = "/queryDictDetail")
    @ApiOperation(value = "查询字典详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "dictId", value = "字典id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询字典详情", code = "queryDictDetail", level = BaseConst.PermLevel.PUBLIC)
    public Result queryDictDetail(@RequestParam Long dictId) {
        return Result.success().data(dictService.queryDictDetail(dictId));
    }

    /**
     * 新增字典
     *
     * @param dictDto 字典
     * @return Result
     */
    @Log
    @PostMapping(value = "/addDict")
    @ApiOperation(value = "新增字典")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "新增字典", code = "addDict")
    public Result addDict(@RequestBody DictDto dictDto) {
        dictService.addDict(dictDto);
        return Result.success();
    }

    /**
     * 编辑字典
     *
     * @param dictDto 字典
     * @return Result
     */
    @Log
    @PostMapping(value = "/editDict")
    @ApiOperation(value = "编辑字典")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "编辑字典", code = "editDict")
    public Result editDict(@RequestBody DictDto dictDto) {
        dictService.editDict(dictDto);
        return Result.success();
    }

    /**
     * 删除字典
     *
     * @param dictId 字典序列
     * @return Result
     */
    @Log
    @PostMapping(value = "/deleteDict")
    @ApiOperation(value = "删除字典")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "dictId", value = "字典id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "删除字典", code = "deleteDict")
    public Result deleteDict(@RequestParam Long dictId) {
        dictService.deleteDict(dictId);
        return Result.success();
    }
}
