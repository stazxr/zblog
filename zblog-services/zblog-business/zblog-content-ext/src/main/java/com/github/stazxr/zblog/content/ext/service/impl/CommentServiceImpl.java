package com.github.stazxr.zblog.content.ext.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.content.ext.domain.dto.CommentAuditDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.CommentQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.Comment;
import com.github.stazxr.zblog.content.ext.domain.enums.CommentStatus;
import com.github.stazxr.zblog.content.ext.domain.error.CommentErrorCode;
import com.github.stazxr.zblog.content.ext.domain.vo.CommentVo;
import com.github.stazxr.zblog.content.ext.mapper.CommentMapper;
import com.github.stazxr.zblog.content.ext.service.CommentService;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 评论管理业务实现层
 *
 * @author SunTao
 * @since 2026-09-01
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    /**
     * 分页查询评论列表
     *
     * @param queryDto 查询参数
     * @return IPage<CommentVo>
     */
    @Override
    public IPage<CommentVo> queryCommentListByPage(CommentQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();

        // 分页查询
        Page<CommentVo> page = new Page<>(queryDto.getPage(), queryDto.getPageSize());
        return baseMapper.selectCommentList(page, queryDto);
    }

    /**
     * 查询评论详情
     *
     * @param commentId 评论id
     * @return CommentVo
     */
    @Override
    public CommentVo queryCommentDetail(Long commentId) {
        CommentVo commentVo = baseMapper.selectCommentDetail(commentId);
        return ThrowUtils.requireNonNull(commentVo, BaseErrorCode.ECOREA001);
    }

    /**
     * 审核评论
     *
     * @param auditDto 评论审核信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditComment(CommentAuditDto auditDto) {
        Comment dbComment = baseMapper.selectById(auditDto.getCommentId());
        boolean commentNotExist = dbComment == null || CommentStatus.DELETED.getValue().equals(dbComment.getStatus());
        ThrowUtils.throwIf(commentNotExist, BaseErrorCode.ECOREA001);
        boolean isPending = CommentStatus.PENDING.getValue().equals(dbComment.getStatus());
        boolean isManual = CommentStatus.MANUAL.getValue().equals(dbComment.getStatus());
        ThrowUtils.throwIf(!isPending && !isManual, CommentErrorCode.ECOMNA001);
        dbComment.setStatus(auditDto.getStatus());
        ThrowUtils.when(!updateById(dbComment)).system(BaseErrorCode.SCOREA002);
        if (dbComment.getParentId() != 0 && CommentStatus.NORMAL.getValue().equals(auditDto.getStatus())) {
            baseMapper.incrementReplyCount(dbComment.getParentId());
        }
    }

    /**
     * 删除评论
     *
     * @param commentId 评论id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long commentId) {
        Comment dbComment = baseMapper.selectById(commentId);
        boolean commentNotExist = dbComment == null || CommentStatus.DELETED.getValue().equals(dbComment.getStatus());
        ThrowUtils.throwIf(commentNotExist, BaseErrorCode.ECOREA001);
        ThrowUtils.when(baseMapper.deleteComment(commentId) != 1).system(BaseErrorCode.SCOREA003);
        if (dbComment.getParentId() != 0) {
            baseMapper.decrementReplyCount(dbComment.getParentId());
        }
    }
}
