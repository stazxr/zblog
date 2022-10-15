package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.domain.dto.query.VersionQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Version;
import com.github.stazxr.zblog.base.service.VersionService;
import com.github.stazxr.zblog.core.annotation.RequestPostSingleParam;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.log.annotation.Log;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class VersionController {
    private final VersionService versionService;

    /**
     * 分页查询版本列表
     *
     * @param queryDto 查询参数
     * @return versionList
     */
    @GetMapping(value = "/pageList")
    @Router(name = "分页查询版本列表", code = "queryVersionListByPage")
    public Result pageList(VersionQueryDto queryDto) {
        return Result.success().data(versionService.queryVersionListByPage(queryDto));
    }

    /**
     * 查询版本信息
     *
     * @param versionId 版本序列
     * @return 版本信息
     */
    @GetMapping(value = "/queryVersionInfo")
    @Router(name = "查询版本信息", code = "queryVersionInfo", level = BaseConst.PermLevel.PUBLIC)
    public Result queryVersionInfo(@RequestParam Long versionId) {
        return Result.success().data(versionService.queryVersionInfo(versionId));
    }

    /**
     * 新增版本
     *
     * @param version 版本
     * @return Result
     */
    @Log
    @PostMapping(value = "/addVersion")
    @Router(name = "新增版本", code = "addVersion")
    public Result addVersion(@RequestBody Version version) {
        versionService.addVersion(version);
        return Result.success();
    }

    /**
     * 编辑版本
     *
     * @param version 版本
     * @return Result
     */
    @Log
    @PostMapping(value = "/editVersion")
    @Router(name = "编辑版本", code = "editVersion")
    public Result editVersion(@RequestBody Version version) {
        versionService.editVersion(version);
        return Result.success();
    }

    /**
     * 删除版本
     *
     * @param versionId 版本ID
     * @return Result
     */
    @Log
    @PostMapping(value = "/deleteVersion")
    @Router(name = "删除版本", code = "deleteVersion")
    public Result deleteVersion(@RequestPostSingleParam Long versionId) {
        versionService.deleteVersion(versionId);
        return Result.success();
    }
}
