package com.github.stazxr.zblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.domain.dto.query.CommentQueryDto;
import com.github.stazxr.zblog.domain.entity.Comment;
import com.github.stazxr.zblog.domain.vo.CommentVo;

import java.util.List;

/**
 * 评论服务层
 *
 * @author SunTao
 * @since 2023-02-03
 */
public interface CommentService extends IService<Comment> {
    /**
     * 分页查询后台评论列表
     *
     * @param queryDto 查询参数
     * @return CommentVoList
     */
    PageInfo<CommentVo> pageCommentList(CommentQueryDto queryDto);

    /**
     * 删除评论
     *
     * @param commentIds 评论列表
     */
    void deleteComment(List<Long> commentIds);

    /**
     * 审核评论
     *
     * @param commentIds 评论列表
     */
    void auditComment(List<Long> commentIds);
}
