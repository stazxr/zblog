package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.core.base.BaseMapper;
import com.github.stazxr.zblog.domain.dto.query.CommentQueryDto;
import com.github.stazxr.zblog.domain.entity.Comment;
import com.github.stazxr.zblog.domain.vo.CommentReplyVo;
import com.github.stazxr.zblog.domain.vo.CommentVo;

import java.util.List;

/**
 * 评论数据持久层
 *
 * @author SunTao
 * @since 2023-02-03
 */
public interface CommentMapper extends BaseMapper<Comment> {
    /**
     * 查询前台评论列表
     *
     * @param queryDto 请求参数
     * @return CommentVoList
     */
    List<CommentVo> selectWebCommentList(CommentQueryDto queryDto);

    /**
     * 查询前台评论回复列表
     *
     * @param queryDto 请求参数
     * @return CommentVoList
     */
    List<CommentReplyVo> selectWebCommentReplyList(CommentQueryDto queryDto);

    /**
     * 分页查询后台评论列表
     *
     * @param queryDto 查询参数
     * @return CommentVoList
     */
    List<CommentVo> selectCommentList(CommentQueryDto queryDto);

    /**
     * 审核评论
     *
     * @param commentIds 评论列表
     */
    void auditComment(List<Long> commentIds);

    /**
     * 获取最新评论列表
     *
     * @return CommentVos
     */
    List<CommentVo> selectBoardLastedCommentList();
}
