package com.github.stazxr.zblog.content.mapper;

import com.github.stazxr.zblog.content.domain.entity.ArticleVersionTagRelation;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 文章发布版本标签关联管理数据层
 *
 * @author SunTao
 * @since 2026-10-26
 */
public interface ArticleVersionTagMapper extends BaseMapper<ArticleVersionTagRelation> {
    /**
     * 记录正式发布的文章标签
     *
     * @param versionId 版本id
     * @param articleId 文章id
     */
    void insertVersionTag(@Param("versionId") Long versionId, @Param("articleId") Long articleId);
}
