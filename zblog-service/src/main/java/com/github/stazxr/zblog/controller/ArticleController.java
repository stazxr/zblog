package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.RequestPostSingleParam;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.domain.dto.ArticleDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleCategoryQueryDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleQueryDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleTagQueryDto;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.service.ArticleCategoryService;
import com.github.stazxr.zblog.service.ArticleService;
import com.github.stazxr.zblog.service.ArticleTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 文章管理
 *
 * @author SunTao
 * @since 2020-11-19
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;

    private final ArticleTagService articleTagService;

    private final ArticleCategoryService articleCategoryService;

    /**
     * 分页查询文章列表
     *
     * @param queryDto 查询参数
     * @return ArticleVoList
     */
    @GetMapping(value = "/pageList")
    @Router(name = "分页查询文章列表", code = "queryArticleListByPage")
    public Result queryArticleListByPage(ArticleQueryDto queryDto) {
        return Result.success().data(articleService.queryArticleListByPage(queryDto));
    }

    /**
     * 查询文章详情
     *
     * @param articleId 文章ID
     * @return ArticleVo
     */
    @Log
    @GetMapping(value = "/queryArticleDetail")
    @Router(name = "查询文章详情", code = "queryArticleDetail")
    public Result queryArticleDetail(Long articleId) {
        return Result.success().data(articleService.queryArticleDetail(articleId));
    }

    /**
     * 新增文章
     *
     * @param articleDto 文章信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/addArticle")
    @Router(name = "新增文章", code = "addArticle")
    public Result addArticle(@RequestBody ArticleDto articleDto) {
        articleService.addArticle(articleDto);
        return Result.success();
    }

    /**
     * 编辑文章
     *
     * @param articleDto 文章信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/editArticle")
    @Router(name = "编辑文章", code = "editArticle")
    public Result editArticle(@RequestBody ArticleDto articleDto) {
        articleService.editArticle(articleDto);
        return Result.success();
    }

    /**
     * 删除文章
     *
     * @param articleId 文章ID
     * @return Result
     */
    @Log
    @PostMapping(value = "/deleteArticle")
    @Router(name = "删除文章", code = "deleteArticle")
    public Result deleteArticle(@RequestPostSingleParam Long articleId) {
        articleService.deleteArticle(articleId);
        return Result.success();
    }

    /**
     * 查询文章分类列表（树）
     *
     * @return CategoryVoList
     */
    @GetMapping(value = "/queryCategoryTree")
    @Router(name = "查询文章分类列表（树）", code = "queryArticleCategoryTree", level = BaseConst.PermLevel.PUBLIC)
    public Result queryCategoryTreeList() {
        return Result.success().data(articleCategoryService.queryCategoryTreeList(new ArticleCategoryQueryDto()));
    }

    /**
     * 查询文章标签列表
     *
     * @return CategoryVoList
     */
    @GetMapping(value = "/queryTagList")
    @Router(name = "查询文章标签列表", code = "queryArticleTagList", level = BaseConst.PermLevel.PUBLIC)
    public Result queryTagList() {
        ArticleTagQueryDto queryDto = new ArticleTagQueryDto();
        queryDto.setEnabled(true);
        return Result.success().data(articleTagService.queryTagList(queryDto));
    }
}
