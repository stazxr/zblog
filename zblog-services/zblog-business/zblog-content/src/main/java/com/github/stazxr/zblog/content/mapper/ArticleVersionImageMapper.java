package com.github.stazxr.zblog.content.mapper;

import com.github.stazxr.zblog.content.domain.entity.ArticleVersionImageRelation;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 文章发布版本封面关联管理数据层
 *
 * @author SunTao
 * @since 2026-10-26
 */
public interface ArticleVersionImageMapper extends BaseMapper<ArticleVersionImageRelation> {
    /**
     * 记录正式发布的文章封面
     *
     * @param versionId 版本id
     * @param articleId 文章id
     */
    void insertVersionImage(@Param("versionId") Long versionId, @Param("articleId") Long articleId);
}
