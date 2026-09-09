package com.github.stazxr.zblog.content.ext.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.content.ext.domain.dto.CommentAuditDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.CommentQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.Comment;
import com.github.stazxr.zblog.content.ext.domain.vo.CommentVo;

/**
 * 评论管理业务层
 *
 * @author SunTao
 * @since 2026-09-01
 */
public interface CommentService extends IService<Comment> {
    /**
     * 分页查询评论列表
     *
     * @param queryDto 查询参数
     * @return IPage<CommentVo>
     */
    IPage<CommentVo> queryCommentListByPage(CommentQueryDto queryDto);

    /**
     * 查询评论详情
     *
     * @param commentId 评论id
     * @return CommentVo
     */
    CommentVo queryCommentDetail(Long commentId);

    /**
     * 审核评论
     *
     * @param auditDto 评论审核信息
     */
    void auditComment(CommentAuditDto auditDto);

    /**
     * 删除评论
     *
     * @param commentId 评论id
     */
    void deleteComment(Long commentId);
}
