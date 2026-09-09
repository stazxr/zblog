package com.github.stazxr.zblog.content.ext.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.content.ext.domain.dto.query.CommentQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.Comment;
import com.github.stazxr.zblog.content.ext.domain.vo.CommentVo;
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

    /**
     * 分页查询评论列表
     *
     * @param queryDto 查询参数
     * @return IPage<CommentVo>
     */
    IPage<CommentVo> selectCommentList(@Param("page") Page<CommentVo> page, @Param("query") CommentQueryDto queryDto);

    /**
     * 查询评论详情
     *
     * @param commentId 评论id
     * @return CommentVo
     */
    CommentVo selectCommentDetail(@Param("commentId") Long commentId);
}
