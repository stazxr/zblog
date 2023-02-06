package com.github.stazxr.zblog.service;

import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.domain.vo.UserVo;
import com.github.stazxr.zblog.domain.CommentLikeDto;
import com.github.stazxr.zblog.domain.dto.CommentDeleteDto;
import com.github.stazxr.zblog.domain.dto.CommentDto;
import com.github.stazxr.zblog.domain.dto.MessageDto;
import com.github.stazxr.zblog.domain.dto.UserLoginDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleQueryDto;
import com.github.stazxr.zblog.domain.dto.query.CommentQueryDto;
import com.github.stazxr.zblog.domain.vo.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    List<TalkVo> queryTalkList();

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
}
