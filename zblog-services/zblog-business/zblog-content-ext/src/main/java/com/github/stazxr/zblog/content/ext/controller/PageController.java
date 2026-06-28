package com.github.stazxr.zblog.content.ext.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.content.ext.domain.dto.PageDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.PageQueryDto;
import com.github.stazxr.zblog.content.ext.domain.vo.PageVo;
import com.github.stazxr.zblog.content.ext.service.PageService;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 页面管理
 *
 * @author SunTao
 * @since 2026-06-12
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pages")
@Api(value = "PageController", tags = {"页面管理"})
public class PageController {
    private final PageService pageService;

    /**
     * 分页查询页面列表
     *
     * @param queryDto 查询参数
     * @return IPage<PageVo>
     */
    @GetMapping(value = "/pageList")
    @ApiOperation(value = "分页查询页面列表")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "分页查询页面列表", code = "PAGEQ001")
    public IPage<PageVo> queryPageListByPage(PageQueryDto queryDto) {
        return pageService.queryPageListByPage(queryDto);
    }

    /**
     * 查询页面列表
     *
     * @param queryDto 查询参数
     * @return List<PageVo>
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "查询页面列表（公共）")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "查询页面列表（公共）", code = "PAGEQ003", level = RouterLevel.PUBLIC)
    public List<PageVo> queryPageList(PageQueryDto queryDto) {
        return pageService.queryPageList(queryDto);
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
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "查询页面详情", code = "PAGEQ002")
    public PageVo queryPageDetail(@RequestParam Long pageId) {
        return pageService.queryPageDetail(pageId);
    }

    /**
     * 新增页面
     *
     * @param pageDto 页面信息
     */
    @Log
    @PostMapping(value = "/addPage")
    @ApiOperation(value = "新增页面")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "新增页面", code = "PAGEA001")
    public void addPage(@RequestBody @Validated(Create.class) PageDto pageDto) {
        pageService.addPage(pageDto);
    }

    /**
     * 编辑页面
     *
     * @param pageDto 页面信息
     */
    @Log
    @PostMapping(value = "/editPage")
    @ApiOperation(value = "编辑页面")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "编辑页面", code = "PAGEU001")
    public void editPage(@RequestBody @Validated(Update.class) PageDto pageDto) {
        pageService.editPage(pageDto);
    }

    /**
     * 删除页面
     *
     * @param pageId 页面id
     */
    @Log
    @PostMapping(value = "/deletePage")
    @ApiOperation(value = "删除页面")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageId", value = "页面id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "删除页面", code = "PAGED001")
    public void deletePage(@RequestParam Long pageId) {
        pageService.deletePage(pageId);
    }
}
