package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.core.base.BaseMapper;
import com.github.stazxr.zblog.domain.entity.ArticleLike;
import org.apache.ibatis.annotations.Param;

/**
 * 文章点赞数据持久层
 *
 * @author SunTao
 * @since 2023-02-07
 */
public interface ArticleLikeMapper extends BaseMapper<ArticleLike> {
    /**
     * 判断用户是否点过赞
     *
     * @param userId    用户ID
     * @param articleId 文章ID
     * @return boolean
     */
    boolean isLiked(@Param("userId") Long userId, @Param("articleId") Long articleId);

    /**
     * 删除点赞记录
     *
     * @param userId    用户ID
     * @param articleId 文章ID
     */
    void deleteLike(@Param("userId") Long userId, @Param("articleId") Long articleId);
}
