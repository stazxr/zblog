package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.core.base.BaseMapper;
import com.github.stazxr.zblog.domain.entity.ArticleTagRelation;
import org.apache.ibatis.annotations.Param;

/**
 * 文章标签中间数据数据持久层
 *
 * @author SunTao
 * @since 2021-01-17
 */
public interface ArticleTagRelationMapper extends BaseMapper<ArticleTagRelation> {
    /**
     * 根据标签序列删除关联数据
     *
     * @param tagId 标签序列
     */
    void deleteByTagId(@Param("tagId") Long tagId);

    /**
     * 根据文章序列删除关联数据
     *
     * @param articleId 文章序列
     */
    void deleteByArticleId(@Param("articleId") Long articleId);
}
