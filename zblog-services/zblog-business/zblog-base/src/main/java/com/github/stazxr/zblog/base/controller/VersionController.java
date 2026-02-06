package com.github.stazxr.zblog.base.controller;

import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.base.domain.dto.VersionDto;
import com.github.stazxr.zblog.base.domain.dto.query.VersionQueryDto;
import com.github.stazxr.zblog.base.domain.vo.VersionVo;
import com.github.stazxr.zblog.base.service.VersionService;
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

/**
 * 版本维护
 *
 * @author SunTao
 * @since 2022-10-14
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/version")
@Api(value = "VersionController", tags = { "版本维护" })
public class VersionController {
    private final VersionService versionService;

    /**
     * 分页查询版本列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<VersionVo>
     */
    @GetMapping(value = "/pageList")
    @ApiOperation("分页查询版本列表")
    @ApiVersion(BaseConst.ApiVersion.V_4_0_0)
    @Router(name = "分页查询版本列表", code = "VERSQ001")
    public PageInfo<VersionVo> pageList(VersionQueryDto queryDto) {
        return versionService.queryVersionListByPage(queryDto);
    }

    /**
     * 查询版本详情
     *
     * @param versionId 版本id
     * @return VersionVo
     */
    @GetMapping(value = "/queryVersionDetail")
    @ApiOperation("查询版本详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "versionId", value = "版本id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(BaseConst.ApiVersion.V_4_0_0)
    @Router(name = "查询版本详情", code = "VERSQ002")
    public VersionVo queryVersionDetail(@RequestParam Long versionId) {
        return versionService.queryVersionDetail(versionId);
    }

    /**
     * 新增版本
     *
     * @param versionDto 版本
     */
    @Log
    @PostMapping(value = "/addVersion")
    @ApiOperation("新增版本")
    @ApiVersion(BaseConst.ApiVersion.V_4_0_0)
    @Router(name = "新增版本", code = "VERSA001")
    public void addVersion(@RequestBody @Validated(Create.class) VersionDto versionDto) {
        versionService.addVersion(versionDto);
    }

    /**
     * 编辑版本
     *
     * @param versionDto 版本
     */
    @Log
    @PostMapping(value = "/editVersion")
    @ApiOperation("编辑版本")
    @ApiVersion(BaseConst.ApiVersion.V_4_0_0)
    @Router(name = "编辑版本", code = "VERSU001")
    public void editVersion(@RequestBody @Validated(Update.class) VersionDto versionDto) {
        versionService.editVersion(versionDto);
    }

    /**
     * 删除版本
     *
     * @param versionId 版本id
     */
    @Log
    @PostMapping(value = "/deleteVersion")
    @ApiOperation("删除版本")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "versionId", value = "版本id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(BaseConst.ApiVersion.V_4_0_0)
    @Router(name = "删除版本", code = "VERSD001")
    public void deleteVersion(@RequestParam Long versionId) {
        versionService.deleteVersion(versionId);
    }
}
