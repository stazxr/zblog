package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.domain.dto.ArticleAuditDto;
import com.github.stazxr.zblog.domain.dto.ArticleDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleCategoryQueryDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleQueryDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleTagQueryDto;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.service.ArticleCategoryService;
import com.github.stazxr.zblog.service.ArticleService;
import com.github.stazxr.zblog.service.ArticleTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
@Api(value = "ArticleController", tags = { "文章控制器" })
public class ArticleController {
    private final ArticleService articleService;

    private final ArticleTagService articleTagService;

    private final ArticleCategoryService articleCategoryService;

    /**
     * 分页查询用户的文章列表
     *
     * @param queryDto 查询参数
     * @return ArticlePageData
     */
    @GetMapping(value = "/pageList")
    @ApiOperation(value = "分页查询用户的文章列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询用户的文章列表", code = "queryArticleListByPage")
    public Result queryArticleListByPage(ArticleQueryDto queryDto) {
        return Result.success().data(articleService.queryArticleListByPage(queryDto));
    }

    /**
     * 分页查询可见的文章列表
     *
     * @param queryDto 查询参数
     * @return ArticlePageData
     */
    @GetMapping(value = "/pagePublicList")
    @ApiOperation(value = "分页查询可见的文章列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询可见的文章列表", code = "queryPublicArticleListByPage")
    public Result queryPublicArticleListByPage(ArticleQueryDto queryDto) {
        return Result.success().data(articleService.queryPublicArticleListByPage(queryDto));
    }

    /**
     * 查询文章详情
     *
     * @param articleId 文章ID
     * @return ArticleVo
     */
    @GetMapping(value = "/queryArticleDetail")
    @ApiOperation(value = "查询文章详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询文章详情", code = "queryArticleDetail", level = BaseConst.PermLevel.PUBLIC)
    public Result queryArticleDetail(@RequestParam Long articleId) {
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
    @ApiOperation(value = "新增文章")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
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
    @ApiOperation(value = "编辑文章")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "编辑文章", code = "editArticle", level = BaseConst.PermLevel.PUBLIC)
    public Result editArticle(@RequestBody ArticleDto articleDto) {
        articleService.editArticle(articleDto);
        return Result.success();
    }

    /**
     * 保存文章草稿
     *
     * @param articleDto 文章信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/saveArticleDraft")
    @ApiOperation(value = "保存文章草稿")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "保存文章草稿", code = "saveArticleDraft", level = BaseConst.PermLevel.PUBLIC)
    public Result saveArticleDraft(@RequestBody ArticleDto articleDto) {
        return Result.success().data(articleService.saveArticleDraft(articleDto));
    }

    /**
     * 查询最新的文章草稿
     *
     * @return ArticleVo
     */
    @GetMapping(value = "/queryLastArticleDraft")
    @ApiOperation(value = "查询最新的文章草稿")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询最新的文章草稿", code = "queryLastArticleDraft", level = BaseConst.PermLevel.PUBLIC)
    public Result queryLastArticleDraft() {
        return Result.success().data(articleService.queryLastArticleDraft());
    }

    /**
     * 文章内容自动保存
     *   建议接口等级为 OPEN, 可以在用户会话过期时，自动保存文章未保存内容，防止编辑内容丢失
     *
     * @param articleDto 文章内容信息
     * @return Result
     */
    @PostMapping(value = "/autoSaveArticleContent")
    @ApiOperation(value = "文章内容自动保存")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "文章内容自动保存", code = "autoSaveArticleContent", level = BaseConst.PermLevel.OPEN)
    public Result autoSaveArticleContent(@RequestBody ArticleDto articleDto) {
        return Result.success().data(articleService.autoSaveArticleContent(articleDto));
    }

    /**
     * 分页查询文章内容自动保存列表
     *
     * @param queryDto 查询参数
     * @return ArticleTmpContentVoList
     */
    @GetMapping(value = "/queryAutoSaveArticleContentByPage")
    @ApiOperation(value = "分页查询文章内容自动保存列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询文章内容自动保存列表", code = "queryAutoSaveArticleContentByPage", level = BaseConst.PermLevel.PUBLIC)
    public Result queryAutoSaveArticleContentByPage(ArticleQueryDto queryDto) {
        return Result.success().data(articleService.queryAutoSaveArticleContentByPage(queryDto));
    }

    /**
     * 查询文章内容自动保存信息
     *
     * @param recordId 保存记录id
     * @return ArticleTmpContentVo
     */
    @GetMapping(value = "/queryAutoSaveArticleContentById")
    @ApiOperation(value = "查询文章内容自动保存信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "recordId", value = "保存记录id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询文章内容自动保存信息", code = "queryAutoSaveArticleContentById", level = BaseConst.PermLevel.PUBLIC)
    public Result queryAutoSaveArticleContentById(@RequestParam Long recordId) {
        return Result.success().data(articleService.queryAutoSaveArticleContentById(recordId));
    }

    /**
     * 文章定时发布
     *
     * @param articleDto 文章信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/publishArticleByTiming")
    @ApiOperation(value = "文章定时发布")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "文章定时发布", code = "publishArticleByTiming", level = BaseConst.PermLevel.PUBLIC)
    public Result publishArticleByTiming(@RequestBody ArticleDto articleDto) {
        articleService.publishArticleByTiming(articleDto);
        return Result.success();
    }

    /**
     * 删除文章
     *
     * @param articleId 文章ID
     * @return Result
     */
    @Log
    @PostMapping(value = "/moveToRecycleBin")
    @ApiOperation(value = "查询文章内容自动保存信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "删除文章", code = "moveArticleToRecycleBin", level = BaseConst.PermLevel.PUBLIC)
    public Result moveArticleToRecycleBin(@RequestParam Long articleId) {
        articleService.moveArticleToRecycleBin(articleId);
        return Result.success();
    }

    /**
     * 彻底删除文章
     *
     * @param articleId 文章ID
     * @return Result
     */
    @Log
    @PostMapping(value = "/deleteArticle")
    @ApiOperation(value = "彻底删除文章")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "彻底删除文章", code = "deleteArticle", level = BaseConst.PermLevel.PUBLIC)
    public Result deleteArticle(@RequestParam Long articleId) {
        articleService.deleteArticle(articleId);
        return Result.success();
    }

    /**
     * 批量删除文章
     *
     * @param articleIds 文章编号列表
     * @return Result
     */
    @Log
    @PostMapping(value = "/batchMoveToRecycleBin")
    @ApiOperation(value = "批量删除文章")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "批量删除文章", code = "batchMoveArticleToRecycleBin", level = BaseConst.PermLevel.PUBLIC)
    public Result batchDeleteArticle(@RequestBody List<Long> articleIds) {
        articleService.batchDeleteArticle(articleIds);
        return Result.success();
    }

    /**
     * 清空回收站
     *
     * @return Result
     */
    @Log
    @PostMapping(value = "/clearRecycleBin")
    @ApiOperation(value = "清空回收站")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "清空回收站", code = "clearArticleRecycleBin", level = BaseConst.PermLevel.PUBLIC)
    public Result clearArticleRecycleBin() {
        articleService.clearArticleRecycleBin();
        return Result.success();
    }

    /**
     * 将文章回收至草稿箱
     *
     * @param articleId 文章ID
     * @return Result
     */
    @Log
    @PostMapping(value = "/recycleToDraftBox")
    @ApiOperation(value = "将文章回收至草稿箱")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "将文章回收至草稿箱", code = "recycleToDraftBox", level = BaseConst.PermLevel.PUBLIC)
    public Result recycleToDraftBox(@RequestParam Long articleId) {
        articleService.recycleToDraftBox(articleId);
        return Result.success();
    }

    /**
     * 审核文章
     *
     * @param auditDto 审核信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/auditArticle")
    @ApiOperation(value = "审核文章")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "审核文章", code = "auditArticle")
    public Result auditArticle(@RequestBody ArticleAuditDto auditDto) {
        articleService.auditArticle(auditDto);
        return Result.success();
    }

    /**
     * 百度推送文章
     *
     * @param articleIds 文章编号列表
     * @return Result
     */
    @Log
    @PostMapping(value = "/pushArticle")
    @ApiOperation(value = "百度推送文章")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "百度推送文章", code = "pushArticle")
    public Result pushArticle(@RequestBody List<Long> articleIds) {
        log.info("articleIds: {}", articleIds);
        return Result.failure();
    }

    /**
     * 下线文章
     *
     * @param articleIds 文章编号列表
     * @return Result
     */
    @Log
    @PostMapping(value = "/offlineArticle")
    @ApiOperation(value = "下线文章")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "下线文章", code = "offlineArticle")
    public Result offlineArticle(@RequestBody List<Long> articleIds) {
        log.info("articleIds: {}", articleIds);
        return Result.failure();
    }

    /**
     * 查询文章分类列表（树）
     *
     * @return CategoryVoList
     */
    @GetMapping(value = "/queryCategoryTree")
    @ApiOperation(value = "查询文章分类列表（树）")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
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
    @ApiOperation(value = "查询文章标签列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询文章标签列表", code = "queryArticleTagList", level = BaseConst.PermLevel.PUBLIC)
    public Result queryTagList() {
        ArticleTagQueryDto queryDto = new ArticleTagQueryDto();
        queryDto.setEnabled(true);
        return Result.success().data(articleTagService.queryTagList(queryDto));
    }

    /**
     * 查询文章默认封面，前台会使用这个接口，需要接口权限为《BaseConst.PermLevel.OPEN》
     *
     * @return ArticleImg
     */
    @GetMapping(value = "/queryArticleDefaultImg")
    @ApiOperation(value = "查询文章默认封面")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询文章默认封面", code = "queryArticleDefaultImg", level = BaseConst.PermLevel.OPEN)
    public Result queryArticleDefaultImg() {
        return Result.success().data(articleService.queryArticleDefaultImg());
    }
}
