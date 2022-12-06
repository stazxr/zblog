package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.RequestPostSingleParam;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.domain.dto.ArticleTagDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleTagQueryDto;
import com.github.stazxr.zblog.domain.enums.ArticleTagType;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.service.ArticleTagService;
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
public class ArticleTagController {
    private final ArticleTagService articleTagService;

    /**
     * 分页查询标签列表
     *
     * @param queryDto 查询参数
     * @return TagVoList
     */
    @GetMapping(value = "/pageList")
    @Router(name = "分页查询标签列表", code = "queryTagListByPage")
    public Result queryTagListByPage(ArticleTagQueryDto queryDto) {
        return Result.success().data(articleTagService.queryTagListByPage(queryDto));
    }

    /**
     * 查询标签详情
     *
     * @param tagId 标签ID
     * @return TagVo
     */
    @Log
    @GetMapping(value = "/queryTagDetail")
    @Router(name = "查询标签详情", code = "queryTagDetail")
    public Result queryTagDetail(Long tagId) {
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
    @Router(name = "删除标签", code = "deleteTag")
    public Result deleteTag(@RequestPostSingleParam Long tagId) {
        articleTagService.deleteTag(tagId);
        return Result.success();
    }
}
