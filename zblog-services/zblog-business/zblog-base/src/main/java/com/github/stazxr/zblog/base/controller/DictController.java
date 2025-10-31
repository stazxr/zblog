package com.github.stazxr.zblog.base.controller;

import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.base.domain.bo.NameValue;
import com.github.stazxr.zblog.base.domain.dto.DictDto;
import com.github.stazxr.zblog.base.domain.dto.query.DictQueryDto;
import com.github.stazxr.zblog.base.domain.vo.DictVo;
import com.github.stazxr.zblog.base.service.DictService;
import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
@Api(value = "DictController", tags = { "字典管理" })
public class DictController {
    private final DictService dictService;

    /**
     * 分页查询字典列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<DictVo>
     */
    @GetMapping(value = "/pageList")
    @ApiOperation(value = "分页查询字典列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询字典列表", code = "DICTQ001")
    public PageInfo<DictVo> queryDictListByPage(DictQueryDto queryDto) {
        return dictService.queryDictListByPage(queryDto);
    }

    /**
     * 查询字典子列表
     *
     * @param pid PID
     * @return List<DictVo>
     */
    @GetMapping(value = "/queryChildList")
    @ApiOperation(value = "查询字典子列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pid", value = "字典pid", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询字典子列表", code = "DICTQ002")
    public List<DictVo> queryChildList(@RequestParam Long pid) {
        return dictService.queryChildList(pid);
    }

    /**
     * 查询字典详情
     *
     * @param dictId 字典序列
     * @return DictVo
     */
    @Log
    @GetMapping(value = "/queryDictDetail")
    @ApiOperation(value = "查询字典详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "dictId", value = "字典id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询字典详情", code = "DICTQ003")
    public DictVo queryDictDetail(@RequestParam Long dictId) {
        return dictService.queryDictDetail(dictId);
    }

    /**
     * 新增字典
     *
     * @param dictDto 字典
     */
    @Log
    @PostMapping(value = "/addDict")
    @ApiOperation(value = "新增字典")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "新增字典", code = "DICTA001")
    public void addDict(@RequestBody @Validated(Create.class) DictDto dictDto) {
        dictService.addDict(dictDto);
    }

    /**
     * 编辑字典
     *
     * @param dictDto 字典
     */
    @Log
    @PostMapping(value = "/editDict")
    @ApiOperation(value = "编辑字典")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "编辑字典", code = "DICTU001")
    public void editDict(@RequestBody @Validated(Update.class) DictDto dictDto) {
        dictService.editDict(dictDto);
    }

    /**
     * 删除字典
     *
     * @param dictId 字典序列
     */
    @Log
    @PostMapping(value = "/deleteDict")
    @ApiOperation(value = "删除字典")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "dictId", value = "字典id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "删除字典", code = "DICTD001")
    public void deleteDict(@RequestParam Long dictId) {
        dictService.deleteDict(dictId);
    }

    /**
     * 根据字典KEY查询配置信息列表
     *
     * @param dictKey 字典KEY
     * @return List<NameValue>
     */
    @GetMapping(value = "/queryConfListByDictKey")
    @ApiOperation(value = "根据字典KEY查询配置信息列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "dictKey", value = "字典key", required = true, dataTypeClass = String.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_5_0_0 })
    @Router(name = "根据字典KEY查询配置信息列表", code = "DICTQ004")
    public List<NameValue> queryConfListByDictKey(@RequestParam String dictKey) {
        return dictService.queryConfListByDictKey(dictKey);
    }

    /**
     * 根据字典KEY查询配置信息
     *
     * @param dictKey 字典KEY
     * @return DictValue
     */
    @GetMapping(value = "/queryDictValueByDictKey")
    @ApiOperation(value = "根据字典KEY查询配置信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "dictKey", value = "字典key", required = true, dataTypeClass = String.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_5_0_0 })
    @Router(name = "根据字典KEY查询配置信息", code = "DICTQ005")
    public String queryDictValueByDictKey(@RequestParam String dictKey) {
        return dictService.queryDictValueByDictKey(dictKey);
    }
}
