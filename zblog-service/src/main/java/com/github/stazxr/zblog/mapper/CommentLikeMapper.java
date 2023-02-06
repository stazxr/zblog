package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.core.base.BaseMapper;
import com.github.stazxr.zblog.domain.entity.CommentLike;
import org.apache.ibatis.annotations.Param;

/**
 * 评论点赞数据持久层
 *
 * @author SunTao
 * @since 2023-02-06
 */
public interface CommentLikeMapper extends BaseMapper<CommentLike> {
    /**
     * 判断用户是否点过赞
     *
     * @param userId    用户ID
     * @param commentId 评论ID
     * @return boolean
     */
    boolean isLiked(@Param("userId") Long userId, @Param("commentId") Long commentId);

    /**
     * 删除点赞记录
     *
     * @param userId    用户ID
     * @param commentId 评论ID
     */
    void deleteLike(@Param("userId") Long userId, @Param("commentId") Long commentId);
}
