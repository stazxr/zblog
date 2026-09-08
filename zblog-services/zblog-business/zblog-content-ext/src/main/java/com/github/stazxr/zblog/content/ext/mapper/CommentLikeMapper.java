package com.github.stazxr.zblog.content.ext.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.stazxr.zblog.content.ext.domain.entity.CommentLike;
import org.apache.ibatis.annotations.Param;

/**
 * 评论点赞管理数据层
 *
 * @author SunTao
 * @since 2026-08-31
 */
public interface CommentLikeMapper extends BaseMapper<CommentLike> {
    /**
     * 判断用户是否点赞
     *
     * @param commentLike 点赞记录
     * @return true: 用户已点赞；false: 用户未点赞
     */
    boolean isLiked(@Param("like") CommentLike commentLike);

    /**
     * 删除用户点赞
     *
     * @param commentLike 点赞记录
     */
    void deleteCommentLike(@Param("like") CommentLike commentLike);
}