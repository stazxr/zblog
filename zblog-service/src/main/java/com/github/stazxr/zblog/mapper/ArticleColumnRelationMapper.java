package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.core.base.BaseMapper;
import com.github.stazxr.zblog.domain.entity.ArticleColumnRelation;
import com.github.stazxr.zblog.domain.vo.ArticleColumnArticleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章专栏中间数据持久层
 *
 * @author SunTao
 * @since 2022-11-22
 */
public interface ArticleColumnRelationMapper extends BaseMapper<ArticleColumnRelation> {
    /**
     * 根据专栏序号删除中间数据
     *
     * @param columnId 专栏id
     */
    void deleteByColumnId(@Param("columnId") Long columnId);

    /**
     * 根据文章序号删除中间数据
     *
     * @param articleId 文章id
     */
    void deleteByArticleId(@Param("articleId") Long articleId);

    /**
     * 查询专栏对应的文章数
     *
     * @param columnId 专栏id
     * @return 文章数
     */
    Integer selectArticleCount(@Param("columnId") Long columnId);

    /**
     * 批量新增专栏文章
     *
     * @param articles 文章列表
     */
    void batchInsert(List<ArticleColumnRelation> articles);

    /**
     * 查询专栏的文章列表
     *
     * @param columnId 专栏id
     * @return ArticleColumnArticleVoList
     */
    List<ArticleColumnArticleVo> selectArticleList(@Param("columnId") Long columnId);
}
