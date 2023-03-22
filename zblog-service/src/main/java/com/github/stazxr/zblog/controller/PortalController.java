package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.domain.dto.*;
import com.github.stazxr.zblog.domain.dto.query.AlbumPhotoQueryDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleQueryDto;
import com.github.stazxr.zblog.domain.dto.query.CommentQueryDto;
import com.github.stazxr.zblog.domain.dto.query.TalkQueryDto;
import com.github.stazxr.zblog.service.PortalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 门户管理
 *
 * @author SunTao
 * @since 2022-11-25
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portal")
@Api(value = "PortalController", tags = { "门户控制器" })
public class PortalController {
    private final PortalService portalService;

    /**
     * 查询博客前台信息
     *
     * @return BlogWebVo
     */
    @GetMapping("/queryBlogInfo")
    @ApiOperation(value = "查询博客前台信息")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询博客前台信息", code = "queryBlogWebInfo", level = BaseConst.PermLevel.OPEN)
    public Result queryBlogWebInfo() {
        return Result.success().data(portalService.queryBlogWebInfo());
    }

    /**
     * 记录访客信息
     *
     * @param request 请求信息
     * @return Result
     */
    @PostMapping(value = "/recordVisitor")
    @ApiOperation(value = "记录访客信息")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "记录访客信息", code = "recordVisitor", level = BaseConst.PermLevel.OPEN)
    public Result recordVisitor(HttpServletRequest request) {
        portalService.recordVisitor(request);
        return Result.success();
    }

    /**
     * 分页查询前台文章列表
     *
     * @param queryDto 查询参数
     * @return ArticleList
     */
    @GetMapping("/queryArticleList")
    @ApiOperation(value = "分页查询前台文章列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询前台文章列表", code = "queryWebArticleList", level = BaseConst.PermLevel.OPEN)
    public Result queryArticleList(ArticleQueryDto queryDto) {
        return Result.success().data(portalService.queryArticleList(queryDto));
    }

    /**
     * 查询前台文章详情
     *
     * @param articleId 文章ID
     * @return ArticleVo
     */
    @GetMapping("/queryArticleDetail")
    @ApiOperation(value = "查询前台文章详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "articleId", value = "文章id", dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询前台文章详情", code = "queryWebArticleDetail", level = BaseConst.PermLevel.OPEN)
    public Result queryArticleDetail(@RequestParam(required = false) Long articleId) {
        return Result.success().data(portalService.queryArticleDetail(articleId));
    }

    /**
     * 查询前台弹幕列表
     *
     * @return MessageVo
     */
    @GetMapping("/queryMessageList")
    @ApiOperation(value = "查询前台弹幕列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询前台弹幕列表", code = "queryWebMessageList", level = BaseConst.PermLevel.OPEN)
    public Result queryMessageList() {
        return Result.success().data(portalService.queryMessageList());
    }

    /**
     * 弹幕留言
     *
     * @param request    请求信息
     * @param messageDto 留言信息
     * @return Result
     */
    @PostMapping(value = "/saveMessage")
    @ApiOperation(value = "弹幕留言")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "弹幕留言", code = "saveMessage", level = BaseConst.PermLevel.OPEN)
    public Result recordVisitor(HttpServletRequest request, @RequestBody MessageDto messageDto) {
        portalService.saveMessage(request, messageDto);
        return Result.success();
    }

    /**
     * 查询前台友链列表
     *
     * @return FriendLinkVo
     */
    @GetMapping("/queryFriendLinkList")
    @ApiOperation(value = "查询前台友链列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询前台友链列表", code = "queryWebFriendLinkList", level = BaseConst.PermLevel.OPEN)
    public Result queryFriendLinkList() {
        return Result.success().data(portalService.queryFriendLinkList());
    }

    /**
     * 前台登录
     *
     * @param request    请求信息
     * @param loginDto   登录信息
     * @return User
     */
    @PostMapping(value = "/webLogin")
    @ApiOperation(value = "前台登录")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "前台登录", code = "webLogin", level = BaseConst.PermLevel.OPEN)
    public Result webLogin(HttpServletRequest request, @RequestBody UserLoginDto loginDto) {
        return Result.success().data(portalService.webLogin(request, loginDto));
    }

    /**
     * 查询前台评论列表
     *
     * @param queryDto 查询参数
     * @return CommentVo
     */
    @GetMapping("/queryCommentList")
    @ApiOperation(value = "查询前台评论列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询前台评论列表", code = "queryWebCommentList", level = BaseConst.PermLevel.OPEN)
    public Result queryCommentList(CommentQueryDto queryDto) {
        return Result.success().data(portalService.queryCommentList(queryDto));
    }

    /**
     * 获取评论回复列表
     *
     * @param queryDto 查询参数
     * @return CommentVo
     */
    @GetMapping("/queryCommentReplyList")
    @ApiOperation(value = "获取评论回复列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "获取评论回复列表", code = "queryCommentReplyList", level = BaseConst.PermLevel.OPEN)
    public Result queryCommentReplyList(CommentQueryDto queryDto) {
        return Result.success().data(portalService.queryCommentReplyList(queryDto));
    }

    /**
     * 新增评论
     *
     * @param request    请求信息
     * @param commentDto 评论信息
     * @return Result
     */
    @PostMapping(value = "/saveComment")
    @ApiOperation(value = "新增评论")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "新增评论", code = "saveComment", level = BaseConst.PermLevel.OPEN)
    public Result saveComment(HttpServletRequest request, @RequestBody CommentDto commentDto) {
        portalService.saveComment(request, commentDto);
        return Result.success();
    }

    /**
     * 点赞评论
     *
     * @param request    请求信息
     * @param commentDto 评论信息
     * @return Result
     */
    @PostMapping(value = "/likeComment")
    @ApiOperation(value = "点赞评论")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "点赞评论", code = "likeComment", level = BaseConst.PermLevel.OPEN)
    public Result saveComment(HttpServletRequest request, @RequestBody CommentLikeDto commentDto) {
        portalService.likeComment(request, commentDto);
        return Result.success();
    }

    /**
     * 回复评论
     *
     * @param request    请求信息
     * @param commentDto 评论信息
     * @return Result
     */
    @PostMapping(value = "/replyComment")
    @ApiOperation(value = "回复评论")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "回复评论", code = "replyComment", level = BaseConst.PermLevel.OPEN)
    public Result replyComment(HttpServletRequest request, @RequestBody CommentDto commentDto) {
        portalService.saveComment(request, commentDto);
        return Result.success();
    }

    /**
     * 删除评论
     *
     * @param commentDto 评论信息
     * @return Result
     */
    @PostMapping(value = "/deleteComment")
    @ApiOperation(value = "删除评论")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "删除评论", code = "deleteWebComment", level = BaseConst.PermLevel.OPEN)
    public Result deleteComment(@RequestBody CommentDeleteDto commentDto) {
        portalService.deleteComment(commentDto);
        return Result.success();
    }

    /**
     * 查询首页轮播的说说列表
     *
     * @return TalkList
     */
    @GetMapping("/queryBoardTalkList")
    @ApiOperation(value = "查询首页轮播的说说列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询首页轮播的说说列表", code = "queryWebBoardTalkList", level = BaseConst.PermLevel.OPEN)
    public Result queryBoardTalkList() {
        return Result.success().data(portalService.queryBoardTalkList());
    }

    /**
     * 获取标签云数据
     *
     * @return CloudTagVos
     */
    @GetMapping("/queryBoardTagList")
    @ApiOperation(value = "获取标签云数据")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "获取标签云数据", code = "queryWebBoardTagList", level = BaseConst.PermLevel.OPEN)
    public Result queryBoardTagList() {
        return Result.success().data(portalService.queryBoardTagList());
    }

    /**
     * 获取热门文章列表
     *
     * @return ArticleVos
     */
    @GetMapping("/queryBoardHotArticleList")
    @ApiOperation(value = "获取热门文章列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "获取热门文章列表", code = "queryWebBoardHotArticleList", level = BaseConst.PermLevel.OPEN)
    public Result queryBoardHotArticleList() {
        return Result.success().data(portalService.queryBoardHotArticleList());
    }

    /**
     * 获取分类专栏列表
     *
     * @return CategoryVos
     */
    @GetMapping("/queryBoardCategoryList")
    @ApiOperation(value = "获取分类专栏列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "获取分类专栏列表", code = "queryWebBoardCategoryList", level = BaseConst.PermLevel.OPEN)
    public Result queryBoardCategoryList() {
        return Result.success().data(portalService.queryBoardCategoryList());
    }

    /**
     * 获取最新评论列表
     *
     * @return CommentVos
     */
    @GetMapping("/queryBoardLastedCommentList")
    @ApiOperation(value = "获取最新评论列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "获取最新评论列表", code = "queryWebBoardLastedCommentList", level = BaseConst.PermLevel.OPEN)
    public Result queryBoardLastedCommentList() {
        return Result.success().data(portalService.queryBoardLastedCommentList());
    }

    /**
     * 查询前台说说列表
     *
     * @param queryDto 查询参数
     * @return TalkList
     */
    @GetMapping("/queryTalkList")
    @ApiOperation(value = "查询前台说说列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询前台说说列表", code = "queryWebTalkList", level = BaseConst.PermLevel.OPEN)
    public Result queryTalkList(TalkQueryDto queryDto) {
        return Result.success().data(portalService.queryTalkList(queryDto));
    }

    /**
     * 查询前台说说详情
     *
     * @param talkId 说说id
     * @return TalkVo
     */
    @GetMapping("/queryTalkById")
    @ApiOperation(value = "查询前台说说详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "talkId", value = "说说id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询前台说说详情", code = "queryWebTalkById", level = BaseConst.PermLevel.OPEN)
    public Result queryTalkById(@RequestParam Long talkId) {
        return Result.success().data(portalService.queryTalkById(talkId));
    }

    /**
     * 点赞说说
     *
     * @param request 请求信息
     * @param talkDto 说说信息
     * @return Result
     */
    @PostMapping(value = "/likeTalk")
    @ApiOperation(value = "点赞说说")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "点赞说说", code = "likeTalk", level = BaseConst.PermLevel.OPEN)
    public Result likeTalk(HttpServletRequest request, @RequestBody TalkLikeDto talkDto) {
        portalService.likeTalk(request, talkDto);
        return Result.success();
    }

    /**
     * 点赞文章
     *
     * @param request    请求信息
     * @param articleDto 文章信息
     * @return Result
     */
    @PostMapping(value = "/likeArticle")
    @ApiOperation(value = "点赞文章")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "点赞文章", code = "likeArticle", level = BaseConst.PermLevel.OPEN)
    public Result likeArticle(HttpServletRequest request, @RequestBody ArticleLikeDto articleDto) {
        portalService.likeArticle(request, articleDto);
        return Result.success();
    }

    /**
     * 浏览文章
     *
     * @param request   请求信息
     * @param articleId 文章id
     * @return Result
     */
    @PostMapping(value = "/viewArticle")
    @ApiOperation(value = "浏览文章")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "浏览文章", code = "viewArticle", level = BaseConst.PermLevel.OPEN)
    public Result viewArticle(HttpServletRequest request, @RequestParam Long articleId) {
        return portalService.viewArticle(request, articleId) ? Result.success() : Result.failure();
    }

    /**
     * 查询前台标签列表
     *
     * @return TagList
     */
    @GetMapping("/queryTagList")
    @ApiOperation(value = "查询前台标签列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询前台标签列表", code = "queryWebTagList", level = BaseConst.PermLevel.OPEN)
    public Result queryTagList() {
        return Result.success().data(portalService.queryTagList());
    }

    /**
     * 查询前台分类列表
     *
     * @return CategoryList
     */
    @GetMapping("/queryCategoryList")
    @ApiOperation(value = "查询前台分类列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询前台分类列表", code = "queryWebCategoryList", level = BaseConst.PermLevel.OPEN)
    public Result queryCategoryList() {
        return Result.success().data(portalService.queryCategoryList());
    }

    /**
     * 查询前台归档列表
     *
     * @param current 页码
     * @return ArticleList
     */
    @GetMapping("/queryArchiveList")
    @ApiOperation(value = "查询前台归档列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "current", value = "页码", dataTypeClass = Integer.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询前台归档列表", code = "queryArchiveList", level = BaseConst.PermLevel.OPEN)
    public Result queryArchiveList(@RequestParam(required = false) Integer current) {
        return Result.success().data(portalService.queryArchiveList(current));
    }

    /**
     * 查询前台分类详情
     *
     * @param categoryId 类别id
     * @return CategoryVo
     */
    @GetMapping("/queryCategoryById")
    @ApiOperation(value = "查询前台分类详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "categoryId", value = "类别id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询前台分类详情", code = "queryWebCategoryById", level = BaseConst.PermLevel.OPEN)
    public Result queryCategoryById(@RequestParam Long categoryId) {
        return Result.success().data(portalService.queryCategoryById(categoryId));
    }

    /**
     * 查询前台标签详情
     *
     * @param tagId 标签id
     * @return TagVo
     */
    @GetMapping("/queryTagById")
    @ApiOperation(value = "查询前台标签详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "tagId", value = "标签id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询前台标签详情", code = "queryWebTagById", level = BaseConst.PermLevel.OPEN)
    public Result queryTagById(@RequestParam Long tagId) {
        return Result.success().data(portalService.queryTagById(tagId));
    }

    /**
     * 根据关键字搜索文章
     *
     * @param keywords 关键字
     * @return ArticleList
     */
    @GetMapping("/searchArticleList")
    @ApiOperation(value = "查询前台标签详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "keywords", value = "关键字", required = true, dataTypeClass = String.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "根据关键字搜索文章", code = "searchArticleList", level = BaseConst.PermLevel.OPEN)
    public Result searchArticleList(@RequestParam String keywords) {
        return Result.success().data(portalService.searchArticleList(keywords));
    }

    /**
     * 查询 Github 贡献日历数据
     *
     * @param username github用户名
     * @return 贡献日历数据
     */
    @GetMapping("/queryGithubCalendarData")
    @ApiOperation(value = "查询Github贡献日历数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "username", value = "github用户名", required = true, dataTypeClass = String.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = "查询 Github 贡献日历数据", code = "queryWebGithubCalendarData", level = BaseConst.PermLevel.OPEN)
    public Result queryGithubCalendarData(@RequestParam String username) {
        return Result.success().data(portalService.queryGithubCalendarData(username));
    }

    /**
     * 查询前台相册列表
     *
     * @return AlbumVo
     */
    @GetMapping("/queryAlbumList")
    @ApiOperation(value = "查询前台相册列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = "查询前台相册列表", code = "queryWebAlbumList", level = BaseConst.PermLevel.OPEN)
    public Result queryAlbumList() {
        return Result.success().data(portalService.queryAlbumList());
    }

    /**
     * 查询前台相册照片列表
     *
     * @param queryDto 查询参数
     * @return AlbumPhotoVo
     */
    @GetMapping("/queryAlbumPhotoList")
    @ApiOperation(value = "查询前台相册照片列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = "查询前台相册照片列表", code = "queryWebAlbumPhotoList", level = BaseConst.PermLevel.OPEN)
    public Result queryAlbumPhotoList(AlbumPhotoQueryDto queryDto) {
        return Result.success().data(portalService.queryAlbumPhotoList(queryDto));
    }
}
