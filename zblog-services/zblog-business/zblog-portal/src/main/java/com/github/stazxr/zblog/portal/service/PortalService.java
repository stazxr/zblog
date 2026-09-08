package com.github.stazxr.zblog.portal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.content.ext.domain.vo.BarrageMessageVo;
import com.github.stazxr.zblog.content.ext.domain.vo.CommentEmojiVo;
import com.github.stazxr.zblog.content.ext.domain.vo.FriendLinkVo;
import com.github.stazxr.zblog.content.ext.domain.vo.ThemePageVo;
import com.github.stazxr.zblog.portal.domain.bo.WebInitInfo;
import com.github.stazxr.zblog.portal.domain.bo.WebLoginUser;
import com.github.stazxr.zblog.portal.domain.dto.ApplyFriendLinkDto;
import com.github.stazxr.zblog.portal.domain.dto.BarrageMessageDto;
import com.github.stazxr.zblog.portal.domain.dto.CommentDto;
import com.github.stazxr.zblog.portal.domain.dto.query.PortalCommentQueryDto;
import com.github.stazxr.zblog.portal.domain.vo.PortalCommentVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 门户管理业务层
 *
 * @author SunTao
 * @since 2026-07-07
 */
public interface PortalService {
    /**
     * 获取网站初始化信息
     *
     * @return WebInitInfo
     */
    WebInitInfo init();

    /**
     * 获取Web端登录用户信息
     *
     * @return WebLoginUser
     */
    WebLoginUser currentWebUserDetail();

    /**
     * 查询博客页面信息
     *
     * @return Map<String, List<ThemePageVo>>
     *     K: pageLabel
     *     V: List<ThemePageVo>
     */
    Map<String, List<ThemePageVo>> queryPageInfo();

    /**
     * 记录访客信息
     *
     * @param request 请求信息
     */
    void recordVisitor(HttpServletRequest request);

    /**
     * 记录访客日志
     *
     * @param request 请求信息
     */
    void recordVisitorLog(HttpServletRequest request);

    /**
     * 查询最新弹幕列表
     *
     * @return List<BarrageMessageVo>
     */
    List<BarrageMessageVo> queryBarrageMessageList();

    /**
     * 新增弹幕
     *
     * @param request 请求信息
     * @param barrageMessageDto 弹幕信息
     */
    void addBarrageMessage(HttpServletRequest request, BarrageMessageDto barrageMessageDto);

    /**
     * 点赞弹幕
     *
     * @param request 请求信息
     * @param barrageMessageId 弹幕id
     * @return boolean true:点赞成功 false:已点赞
     */
    boolean likeBarrageMessage(HttpServletRequest request, Long barrageMessageId);

    /**
     * 查询前台友链列表
     *
     * @return Map<String, List<FriendLinkVo>>
     */
    Map<String, List<FriendLinkVo>> queryFriendLinkList();

    /**
     * 友链申请
     *
     * @param friendLinkDto 友链信息
     */
    void applyFriendLink(ApplyFriendLinkDto friendLinkDto);

    /**
     * 记录友链点击日志
     *
     * @param request 请求信息
     * @param friendLinkId 友链id
     */
    void recordFriendLinkClickLog(HttpServletRequest request, Long friendLinkId);

    /**
     * 查询评论表情包
     *
     * @return List<CommentEmojiVo>
     */
    List<CommentEmojiVo> queryCommentImageList();

    /**
     * 查询前台评论总数
     *
     * @param queryDto 查询参数
     * @return Long 评论总数
     */
    Long queryCommentTotal(PortalCommentQueryDto queryDto);

    /**
     * 查询前台评论列表
     *
     * @param queryDto 查询参数
     * @return IPage<PortalCommentVo>
     */
    IPage<PortalCommentVo> queryCommentList(PortalCommentQueryDto queryDto);

    /**
     * 查询前台评论回复列表
     *
     * @param queryDto 查询参数
     * @return IPage<PortalCommentVo>
     */
    IPage<PortalCommentVo> queryCommentReplyList(PortalCommentQueryDto queryDto);

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
     * @param request   请求信息
     * @param commentId 评论id
     * @return true: 点赞/取消点赞成功; false: 点赞/取消点赞失败
     */
    boolean likeComment(HttpServletRequest request, Long commentId);

    /**
     * 删除评论
     *
     * @param commentId 评论id
     */
    void deleteComment(Long commentId);
}
