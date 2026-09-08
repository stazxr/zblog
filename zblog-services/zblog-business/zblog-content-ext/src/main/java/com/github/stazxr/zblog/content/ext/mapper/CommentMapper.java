package com.github.stazxr.zblog.content.ext.mapper;

import com.github.stazxr.zblog.content.ext.domain.entity.Comment;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 评论管理数据层
 *
 * @author SunTao
 * @since 2026-08-31
 */
public interface CommentMapper extends BaseMapper<Comment> {
    /**
     * 回复数减一
     *
     * @param commentId 评论id
     */
    void decrementReplyCount(@Param("commentId") Long commentId);

    /**
     * 回复数加一
     *
     * @param commentId 评论id
     */
    void incrementReplyCount(@Param("commentId") Long commentId);

    /**
     * 点赞数减一
     *
     * @param commentId 评论id
     */
    void decrementLikeCount(@Param("commentId") Long commentId);

    /**
     * 点赞数加一
     *
     * @param commentId 评论id
     */
    void incrementLikeCount(@Param("commentId") Long commentId);

    /**
     * 删除评论
     *
     * @param commentId 评论id
     * @return 影响行数
     */
    int deleteComment(@Param("commentId") Long commentId);

//
//    /**
//     * 分页查询后台评论列表
//     *
//     * @param queryDto 查询参数
//     * @return CommentVoList
//     */
//    List<CommentVo> selectCommentList(CommentQueryDto queryDto);
//
//    /**
//     * 审核评论
//     *
//     * @param commentIds 评论列表
//     */
//    void auditComment(List<Long> commentIds);
}
