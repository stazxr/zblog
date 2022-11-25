package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.core.base.BaseMapper;
import com.github.stazxr.zblog.domain.entity.ArticleColumnRelation;
import org.apache.ibatis.annotations.Param;

/**
 * 文章栏目中间数据持久层
 *
 * @author SunTao
 * @since 2022-11-22
 */
public interface ArticleColumnRelationMapper extends BaseMapper<ArticleColumnRelation> {
    /**
     * 根据栏目序号删除中间数据
     *
     * @param columnId 栏目ID
     */
    void deleteByColumnId(@Param("columnId") Long columnId);

    /**
     * 根据文章序号删除中间数据
     *
     * @param articleId 文章ID
     */
    void deleteByArticleId(@Param("articleId") Long articleId);
}
