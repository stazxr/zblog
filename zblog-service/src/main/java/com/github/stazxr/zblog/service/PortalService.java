package com.github.stazxr.zblog.service;

import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.domain.vo.UserVo;
import com.github.stazxr.zblog.domain.bo.GitCalendarData;
import com.github.stazxr.zblog.domain.dto.*;
import com.github.stazxr.zblog.domain.dto.query.AlbumPhotoQueryDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleQueryDto;
import com.github.stazxr.zblog.domain.dto.query.CommentQueryDto;
import com.github.stazxr.zblog.domain.dto.query.TalkQueryDto;
import com.github.stazxr.zblog.domain.vo.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 前台服务层
 *
 * @author SunTao
 * @since 2022-11-25
 */
public interface PortalService {
    /**
     * 查询博客前台信息
     *
     * @return BlogWebVo
     */
    BlogWebVo queryBlogWebInfo();

    /**
     * 记录访客信息
     *
     * @param request 请求信息
     */
    void recordVisitor(HttpServletRequest request);

    /**
     * 查询首页轮播的说说列表
     *
     * @return TalkList
     */
    List<TalkVo> queryBoardTalkList();

    /**
     * 分页查询前台文章列表
     *
     * @param queryDto 查询参数
     * @return ArticleList
     */
    PageInfo<ArticleVo> queryArticleList(ArticleQueryDto queryDto);

    /**
     * 分页查询前台文章详情
     *
     * @param articleId 文章ID
     * @return ArticleVo
     */
    ArticleVo queryArticleDetail(Long articleId);

    /**
     * 留言板留言
     *
     * @param request    请求信息
     * @param messageDto 留言信息
     */
    void saveMessage(HttpServletRequest request, MessageDto messageDto);

    /**
     * 查询前台弹幕列表
     *
     * @return MessageVo
     */
    List<MessageVo> queryMessageList();

    /**
     * 查询前台友链列表
     *
     * @return FriendLinkVo
     */
    List<FriendLinkVo> queryFriendLinkList();

    /**
     * 前台登录
     *
     * @param request    请求信息
     * @param loginDto   登录信息
     * @return User
     */
    UserVo webLogin(HttpServletRequest request, UserLoginDto loginDto);

    /**
     * 查询前台评论列表
     *
     * @param queryDto 查询参数
     * @return CommentVo
     */
    PageInfo<CommentVo> queryCommentList(CommentQueryDto queryDto);

    /**
     * 获取评论回复列表
     *
     * @param queryDto 查询参数
     * @return CommentReplyVo
     */
    PageInfo<CommentReplyVo> queryCommentReplyList(CommentQueryDto queryDto);

    /**
     * 新增评论
     *
     * @param request    请求信息
     * @param commentDto 评论信息
     */
    void saveComment(HttpServletRequest request, CommentDto commentDto);

    /**
     * 点赞评论
     *
     * @param request    请求信息
     * @param commentDto 评论信息
     */
    void likeComment(HttpServletRequest request, CommentLikeDto commentDto);

    /**
     * 删除评论
     *
     * @param commentDto 评论信息
     */
    void deleteComment(CommentDeleteDto commentDto);

    /**
     * 查询前台说说列表
     *
     * @param queryDto 查询参数
     * @return TalkList
     */
    PageInfo<TalkVo> queryTalkList(TalkQueryDto queryDto);

    /**
     * 点赞说说
     *
     * @param request 请求信息
     * @param talkDto 说说信息
     */
    void likeTalk(HttpServletRequest request, TalkLikeDto talkDto);

    /**
     * 点赞文章
     *
     * @param request    请求信息
     * @param articleDto 文章信息
     */
    void likeArticle(HttpServletRequest request, ArticleLikeDto articleDto);

    /**
     * 查询前台说说详情
     *
     * @param talkId 查询详情
     * @return TalkVo
     */
    TalkVo queryTalkById(Long talkId);

    /**
     * 查询前台标签列表
     *
     * @return TagList
     */
    List<ArticleTagVo> queryTagList();

    /**
     * 查询前台分类列表
     *
     * @return CategoryList
     */
    List<ArticleCategoryVo> queryCategoryList();

    /**
     * 查询前台归档列表
     *
     * @param current 页码
     * @return ArticleList
     */
    PageInfo<ArticleVo> queryArchiveList(Integer current);

    /**
     * 查询前台分类详情
     *
     * @param categoryId 查询详情
     * @return CategoryVo
     */
    ArticleCategoryVo queryCategoryById(Long categoryId);

    /**
     * 查询前台标签详情
     *
     * @param tagId 查询详情
     * @return TagVo
     */
    ArticleTagVo queryTagById(Long tagId);

    /**
     * 浏览文章
     *
     * @param request   请求信息
     * @param articleId 文章序列
     * @return 是否新增成功
     */
    boolean viewArticle(HttpServletRequest request, Long articleId);

    /**
     * 根据关键字搜索文章
     *
     * @param keywords 查询关键字
     * @return ArticleList
     */
    List<ArticleVo> searchArticleList(String keywords);

    /**
     * 获取标签云数据
     *
     * @return CloudTagVos
     */
    List<CloudTagVo> queryBoardTagList();

    /**
     * 获取热门文章列表
     *
     * @return ArticleVos
     */
    List<ArticleVo> queryBoardHotArticleList();

    /**
     * 获取分类专栏列表
     *
     * @return CategoryVos
     */
    List<ArticleCategoryVo> queryBoardCategoryList();

    /**
     * 获取最新评论列表
     *
     * @return CommentVos
     */
    List<CommentVo> queryBoardLastedCommentList();

    /**
     * 查询 Github 贡献日历数据
     *
     * @param username Github 用户名
     * @return 贡献日历数据
     */
    GitCalendarData queryGithubCalendarData(String username);

    /**
     * 查询前台相册列表
     *
     * @return AlbumVo
     */
    List<AlbumVo> queryAlbumList();

    /**
     * 查询前台相册照片列表
     *
     * @param queryDto 查询参数
     * @return PortalAlbumPhotoVo
     */
    PortalAlbumPhotoVo queryAlbumPhotoList(AlbumPhotoQueryDto queryDto);
}
