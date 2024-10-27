package com.github.stazxr.zblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.domain.dto.query.CommentQueryDto;
import com.github.stazxr.zblog.domain.entity.Comment;
import com.github.stazxr.zblog.domain.vo.CommentVo;
import com.github.stazxr.zblog.mapper.CommentMapper;
import com.github.stazxr.zblog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 评论服务实现层
 *
 * @author SunTao
 * @since 2023-02-03
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    /**
     * 分页查询后台评论列表
     *
     * @param queryDto 查询参数
     * @return CommentVoList
     */
    @Override
    public PageInfo<CommentVo> pageCommentList(CommentQueryDto queryDto) {
        queryDto.checkPage();
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new PageInfo<>(baseMapper.selectCommentList(queryDto));
    }

    /**
     * 删除评论
     *
     * @param commentIds 评论列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(List<Long> commentIds) {
        if (commentIds != null && commentIds.size() > 0) {
            baseMapper.deleteBatchIds(commentIds);
        }
    }

    /**
     * 审核评论
     *
     * @param commentIds 评论列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditComment(List<Long> commentIds) {
        if (commentIds != null && commentIds.size() > 0) {
            baseMapper.auditComment(commentIds);
        }
    }
}
