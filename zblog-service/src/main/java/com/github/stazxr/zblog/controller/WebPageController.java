package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.domain.dto.PageDto;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.service.PageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 页面管理模块
 *
 * @author SunTao
 * @since 2022-12-14
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pages")
@Api(value = "WebPageController", tags = { "页面控制器" })
public class WebPageController {
    private final PageService pageService;

    /**
     * 查询页面配置列表
     *
     * @return PageVoList
     */
    @GetMapping(value = "/queryPageList")
    @ApiOperation(value = "查询页面配置列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询页面配置列表", code = "queryPageList")
    public Result queryPageList() {
        return Result.success().data(pageService.queryPageList());
    }

    /**
     * 查询页面详情
     *
     * @param pageId 页面id
     * @return PageVo
     */
    @GetMapping(value = "/queryPageDetail")
    @ApiOperation(value = "查询页面详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageId", value = "页面id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询页面详情", code = "queryPageDetail", level = BaseConst.PermLevel.PUBLIC)
    public Result queryPageDetail(@RequestParam Long pageId) {
        return Result.success().data(pageService.queryPageDetail(pageId));
    }

    /**
     * 新增或编辑页面
     *
     * @param pageDto 页面信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/addOrEditPage")
    @ApiOperation(value = "新增或编辑页面")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "新增或编辑页面", code = "addOrEditPage")
    public Result addOrEditPage(@RequestBody PageDto pageDto) {
        pageService.addOrEditPage(pageDto);
        return Result.success();
    }

    /**
     * 删除页面
     *
     * @param pageId 页面ID
     * @return Result
     */
    @Log
    @PostMapping(value = "/deletePage")
    @ApiOperation(value = "删除页面")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageId", value = "页面id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "删除页面", code = "deletePage")
    public Result deletePage(@RequestParam Long pageId) {
        pageService.deletePage(pageId);
        return Result.success();
    }
}
