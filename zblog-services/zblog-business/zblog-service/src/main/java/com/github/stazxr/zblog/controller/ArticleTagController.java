package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.domain.dto.ArticleTagDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleTagQueryDto;
import com.github.stazxr.zblog.domain.enums.ArticleTagType;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.service.ArticleTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 文章标签管理
 *
 * @author SunTao
 * @since 2020-11-19
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
@Api(value = "ArticleTagController", tags = { "标签控制器" })
public class ArticleTagController {
    private final ArticleTagService articleTagService;

    /**
     * 分页查询标签列表
     *
     * @param queryDto 查询参数
     * @return TagVoList
     */
    @GetMapping(value = "/pageList")
    @ApiOperation(value = "分页查询标签列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询标签列表", code = "queryTagListByPage")
    public Result queryTagListByPage(ArticleTagQueryDto queryDto) {
        return Result.success().data(articleTagService.queryTagListByPage(queryDto));
    }

    /**
     * 查询标签详情
     *
     * @param tagId 标签id
     * @return TagVo
     */
    @GetMapping(value = "/queryTagDetail")
    @ApiOperation(value = "查询标签详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "tagId", value = "标签id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询标签详情", code = "queryTagDetail")
    public Result queryTagDetail(@RequestParam Long tagId) {
        return Result.success().data(articleTagService.queryTagDetail(tagId));
    }

    /**
     * 新增标签
     *
     * @param tagDto 标签信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/addTag")
    @ApiOperation(value = "新增标签")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "新增标签", code = "addTag")
    public Result addTag(@RequestBody ArticleTagDto tagDto) {
        tagDto.setType(ArticleTagType.COMMON.getType());
        articleTagService.addTag(tagDto);
        return Result.success();
    }

    /**
     * 编辑标签
     *
     * @param tagDto 标签信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/editTag")
    @ApiOperation(value = "编辑标签")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "编辑标签", code = "editTag")
    public Result editTag(@RequestBody ArticleTagDto tagDto) {
        articleTagService.editTag(tagDto);
        return Result.success();
    }

    /**
     * 删除标签
     *
     * @param tagId 标签ID
     * @return Result
     */
    @Log
    @PostMapping(value = "/deleteTag")
    @ApiOperation(value = "删除标签")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "tagId", value = "标签id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "删除标签", code = "deleteTag")
    public Result deleteTag(@RequestParam Long tagId) {
        articleTagService.deleteTag(tagId);
        return Result.success();
    }
}
