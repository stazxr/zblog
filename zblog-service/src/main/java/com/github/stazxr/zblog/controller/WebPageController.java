package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.RequestPostSingleParam;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.domain.dto.PageDto;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.service.PageService;
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
public class WebPageController {
    private final PageService pageService;

    /**
     * 查询页面配置列表
     *
     * @return PageVoList
     */
    @GetMapping(value = "/queryPageList")
    @Router(name = "查询页面配置列表", code = "queryPageList")
    public Result queryPageList() {
        return Result.success().data(pageService.queryPageList());
    }

    /**
     * 查询页面详情
     *
     * @param pageId 页面ID
     * @return PageVo
     */
    @GetMapping(value = "/queryPageDetail")
    @Router(name = "查询页面详情", code = "queryPageDetail", level = BaseConst.PermLevel.PUBLIC)
    public Result queryPageDetail(Long pageId) {
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
    @Router(name = "删除页面", code = "deletePage")
    public Result deletePage(@RequestPostSingleParam Long pageId) {
        pageService.deletePage(pageId);
        return Result.success();
    }
}
