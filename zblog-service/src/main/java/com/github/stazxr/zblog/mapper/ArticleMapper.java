package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.core.base.BaseMapper;
import com.github.stazxr.zblog.domain.dto.query.ArticleQueryDto;
import com.github.stazxr.zblog.domain.entity.Article;
import com.github.stazxr.zblog.domain.enums.ArticleStatus;
import com.github.stazxr.zblog.domain.vo.ArticleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章数据持久层
 *
 * @author SunTao
 * @since 2021-02-23
 */
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 查询文章列表
     *
     * @param queryDto 查询参数
     * @return ArticleVoList
     */
    List<ArticleVo> selectArticleList(ArticleQueryDto queryDto);

    /**
     * 查询文章详情
     *
     * @param articleId 文章ID
     * @return ArticleVo
     */
    ArticleVo selectArticleDetail(@Param("articleId") Long articleId);

    /**
     * 修改文章状态
     *
     * @param articleId 文章序列
     * @param status    文章状态
     */
    void updateArticleStatus(@Param("articleId") Long articleId, @Param("status") ArticleStatus status);
}
