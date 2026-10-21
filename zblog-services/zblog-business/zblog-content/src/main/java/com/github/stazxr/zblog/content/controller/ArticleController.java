package com.github.stazxr.zblog.content.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.content.domain.dto.ArticleAutoSaveDto;
import com.github.stazxr.zblog.content.domain.dto.ArticleDraftDto;
import com.github.stazxr.zblog.content.domain.dto.ArticleDto;
import com.github.stazxr.zblog.content.domain.dto.query.ArticleAutoSaveQueryDto;
import com.github.stazxr.zblog.content.domain.dto.query.ArticleQueryDto;
import com.github.stazxr.zblog.content.domain.vo.ArticleAutoSaveVo;
import com.github.stazxr.zblog.content.domain.vo.ArticleCountVo;
import com.github.stazxr.zblog.content.domain.vo.ArticleVo;
import com.github.stazxr.zblog.content.service.ArticleService;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 文章管理
 *
 * @author SunTao
 * @since 2020-11-19
 * @since 2026-04-12 代码重构
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
@Api(value = "ArticleController", tags = { "文章管理" })
public class ArticleController {
    private final ArticleService articleService;

    /**
     * 分页查询我的文章列表
     *
     * @param queryDto 查询参数
     * @return IPage<ArticleVo>
     */
    @GetMapping(value = "/pageMyList")
    @ApiOperation(value = "分页查询我的文章列表")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "分页查询我的文章列表", code = "ARTIQ001", level = RouterLevel.PUBLIC)
    public IPage<ArticleVo> queryMyArticleListByPage(ArticleQueryDto queryDto) {
        return articleService.queryMyArticleListByPage(queryDto);
    }

    /**
     * 查询我的文章数量统计信息
     *
     * @return ArticleCountVo
     */
    @GetMapping(value = "/queryMyArticleCountInfo")
    @ApiOperation(value = "查询我的文章数量统计信息")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "查询我的文章数量统计信息", code = "ARTIQ002", level = RouterLevel.PUBLIC)
    public ArticleCountVo queryMyArticleCountInfo() {
        return articleService.queryMyArticleCountInfo();
    }

    /**
     * 查询我的文章详情
     *
     * @param articleId 文章id
     * @return ArticleVo
     */
    @GetMapping(value = "/queryMyArticleDetail")
    @ApiOperation(value = "查询我的文章详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "查询我的文章详情", code = "ARTIQ006", level = RouterLevel.PUBLIC)
    public ArticleVo queryMyArticleDetail(@RequestParam Long articleId) {
        return articleService.queryMyArticleDetail(articleId);
    }

    /**
     * 新增文章
     *
     * @param articleDto 文章信息
     */
    @Log
    @PostMapping(value = "/addArticle")
    @ApiOperation(value = "新增文章")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "新增文章", code = "ARTIA001")
    public void addArticle(@RequestBody @Validated(Create.class) ArticleDto articleDto) {
        articleService.addArticle(articleDto);
    }

    /**
     * 编辑文章
     *
     * @param articleDto 文章信息
     */
    @Log
    @PostMapping(value = "/editArticle")
    @ApiOperation(value = "编辑文章")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "编辑文章", code = "ARTIU001")
    public void editArticle(@RequestBody @Validated(Update.class) ArticleDto articleDto) {
        articleService.editArticle(articleDto);
    }

    /**
     * 删除文章（移动到回收站）
     *
     * @param articleId 文章id
     */
    @Log
    @PostMapping(value = "/deleteArticle")
    @ApiOperation(value = "删除文章（移动到回收站）")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "删除文章（移动到回收站）", code = "ARTID001", level = RouterLevel.PUBLIC)
    public void deleteArticle(@RequestParam Long articleId) {
        articleService.deleteArticle(articleId);
    }

    /**
     * 删除文章（彻底删除）
     *
     * @param articleId 文章id
     */
    @Log
    @PostMapping(value = "/deleteArticle2")
    @ApiOperation(value = "删除文章（彻底删除）")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "删除文章（彻底删除）", code = "ARTID002", level = RouterLevel.PUBLIC)
    public void deleteArticle2(@RequestParam Long articleId) {
        articleService.deleteArticle2(articleId);
    }

    /**
     * 删除文章（清空回收站）
     */
    @Log
    @PostMapping(value = "/deleteArticle3")
    @ApiOperation(value = "删除文章（清空回收站）")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "删除文章（清空回收站）", code = "ARTID003", level = RouterLevel.PUBLIC)
    public void deleteArticle3() {
        articleService.deleteArticle3();
    }

//    /**
//     * 将文章回收至草稿箱
//     *
//     * @param articleId 文章ID
//     * @return Result
//     */
//    @Log
//    @PostMapping(value = "/recycleToDraftBox")
//    @ApiOperation(value = "将文章回收至草稿箱")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataTypeClass = Long.class)
//    })
//    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
//    @Router(name = "将文章回收至草稿箱", code = "recycleToDraftBox", level = RouterLevel.PUBLIC)
//    public Result recycleToDraftBox(@RequestParam Long articleId) {
//        articleService.recycleToDraftBox(articleId);
//        return Result.success();
//    }
//

    /**
     * 文章内容自动保存
     *
     *   <p>建议接口等级为 OPEN, 可以在用户会话过期时，自动保存文章未保存内容，防止编辑内容丢失
     *
     * @param autoSaveDto 文章自动保存信息
     * @return 自动保存时间
     */
    @PostMapping(value = "/autoSaveArticleContent")
    @ApiOperation(value = "编辑文章")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "文章内容自动保存", code = "ARTIA002", level = RouterLevel.OPEN)
    public String autoSaveArticleContent(@RequestBody ArticleAutoSaveDto autoSaveDto) {
        return articleService.autoSaveArticleContent(autoSaveDto);
    }

    /**
     * 分页查询文章内容自动保存列表
     *
     * @param queryDto 查询参数
     * @return IPage<ArticleAutoSaveVo>
     */
    @GetMapping(value = "/queryAutoSaveArticleContentByPage")
    @ApiOperation(value = "分页查询文章内容自动保存列表")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "分页查询文章内容自动保存列表", code = "ARTIQ003", level = RouterLevel.PUBLIC)
    public IPage<ArticleAutoSaveVo> queryAutoSaveArticleContentByPage(ArticleAutoSaveQueryDto queryDto) {
        return articleService.queryAutoSaveArticleContentByPage(queryDto);
    }

    /**
     * 查询文章内容自动保存详情
     *
     * @param recordId 保存记录id
     * @return ArticleAutoSaveVo
     */
    @GetMapping(value = "/queryAutoSaveArticleContentById")
    @ApiOperation(value = "查询文章内容自动保存信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "recordId", value = "保存记录id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "查询文章内容自动保存详情", code = "ARTIQ004", level = RouterLevel.PUBLIC)
    public ArticleAutoSaveVo queryAutoSaveArticleContentById(@RequestParam Long recordId) {
        return articleService.queryAutoSaveArticleContentById(recordId);
    }

    /**
     * 保存文章草稿
     *
     * @param articleDto 文章信息
     */
    @Log
    @PostMapping(value = "/saveArticleDraft")
    @ApiOperation(value = "保存文章草稿")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "保存文章草稿", code = "ARTIA003", level = RouterLevel.PUBLIC)
    public void saveArticleDraft(@RequestBody @Validated ArticleDraftDto articleDto) {
        articleService.saveArticleDraft(articleDto);
    }

    /**
     * 查询最新的文章草稿
     *
     * @return ArticleVo
     */
    @GetMapping(value = "/queryLastArticleDraft")
    @ApiOperation(value = "查询最新的文章草稿")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "查询最新的文章草稿", code = "ARTIQ005", level = RouterLevel.PUBLIC)
    public ArticleVo queryLastArticleDraft() {
        return articleService.queryLastArticleDraft();
    }


//    /**
//     * 分页查询可见的文章列表
//     *
//     * @param queryDto 查询参数
//     * @return ArticlePageData
//     */
//    @GetMapping(value = "/pagePublicList")
//    @ApiOperation(value = "分页查询可见的文章列表")
//    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
//    @Router(name = "分页查询可见的文章列表", code = "queryPublicArticleListByPage")
//    public Result queryPublicArticleListByPage(ArticleQueryDto queryDto) {
//        return Result.success().data(articleService.queryPublicArticleListByPage(queryDto));
//    }
//
//    /**
//     * 文章定时发布
//     *
//     * @param articleDto 文章信息
//     * @return Result
//     */
//    @Log
//    @PostMapping(value = "/publishArticleByTiming")
//    @ApiOperation(value = "文章定时发布")
//    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
//    @Router(name = "文章定时发布", code = "publishArticleByTiming", level = RouterLevel.PUBLIC)
//    public Result publishArticleByTiming(@RequestBody ArticleDto articleDto) {
//        articleService.publishArticleByTiming(articleDto);
//        return Result.success();
//    }
//    /**
//     * 审核文章
//     *
//     * @param auditDto 审核信息
//     * @return Result
//     */
//    @Log
//    @PostMapping(value = "/auditArticle")
//    @ApiOperation(value = "审核文章")
//    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
//    @Router(name = "审核文章", code = "auditArticle")
//    public Result auditArticle(@RequestBody ArticleAuditDto auditDto) {
//        articleService.auditArticle(auditDto);
//        return Result.success();
//    }
//
//    /**
//     * 百度推送文章
//     *
//     * @param articleIds 文章编号列表
//     * @return Result
//     */
//    @Log
//    @PostMapping(value = "/pushArticle")
//    @ApiOperation(value = "百度推送文章")
//    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
//    @Router(name = "百度推送文章", code = "pushArticle")
//    public Result pushArticle(@RequestBody List<Long> articleIds) {
//        log.info("articleIds: {}", articleIds);
//        return Result.failure();
//    }
//
//    /**
//     * 下线文章
//     *
//     * @param articleIds 文章编号列表
//     * @return Result
//     */
//    @Log
//    @PostMapping(value = "/offlineArticle")
//    @ApiOperation(value = "下线文章")
//    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
//    @Router(name = "下线文章", code = "offlineArticle")
//    public Result offlineArticle(@RequestBody List<Long> articleIds) {
//        log.info("articleIds: {}", articleIds);
//        return Result.failure();
//    }
}
