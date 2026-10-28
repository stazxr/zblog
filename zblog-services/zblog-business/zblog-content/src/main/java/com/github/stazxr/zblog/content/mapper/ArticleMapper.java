package com.github.stazxr.zblog.content.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.content.domain.dto.query.ArticleQueryDto;
import com.github.stazxr.zblog.content.domain.entity.Article;
import com.github.stazxr.zblog.content.domain.vo.ArticleCountVo;
import com.github.stazxr.zblog.content.domain.vo.ArticleCoverImageVo;
import com.github.stazxr.zblog.content.domain.vo.ArticleTagNameVo;
import com.github.stazxr.zblog.content.domain.vo.ArticleVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章管理数据层
 *
 * @author SunTao
 * @since 2021-02-23
 */
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 分页查询我的文章列表
     *
     * @param page     分页参数
     * @param queryDto 查询参数
     * @return IPage<ArticleVo>
     */
    IPage<ArticleVo> selectMyArticleList(@Param("page") Page<ArticleVo> page, @Param("query") ArticleQueryDto queryDto);

    /**
     * 批量查询文章标签名称列表
     *
     * @param articleIds 文章id列表
     * @return List<ArticleTagNameVo>
     */
    List<ArticleTagNameVo> selectArticleTagNamesByArticleIds(@Param("articleIds") List<Long> articleIds);

    /**
     * 批量查询文章封面列表
     *
     * @param articleIds 文章id列表
     * @return List<ArticleCoverImageVo>
     */
    List<ArticleCoverImageVo> selectArticleCoverImagesByArticleIds(@Param("articleIds") List<Long> articleIds);

    /**
     * 查询我的文章数量统计信息
     *
     * @param authorId 作者ID
     * @return ArticleCountVo
     */
    ArticleCountVo selectMyArticleCountInfo(@Param("authorId") Long authorId);

    /**
     * 查询文章详情
     *
     * @param articleId 文章id
     * @return ArticleVo
     */
    ArticleVo selectArticleDetail(@Param("articleId") Long articleId);

    /**
     * 查询最新的空文章草稿
     *
     * @param authorId 作者id
     * @return ArticleVo
     */
    ArticleVo selectLastArticleEmptyDraft(@Param("authorId") Long authorId);

    /**
     * 查询最新的文章草稿
     *
     * @param authorId 作者id
     * @return ArticleVo
     */
    ArticleVo selectLastArticleDraft(@Param("authorId") Long authorId);

    /**
     * 查询回收站的文章id列表
     *
     * @param authorId 作者id
     * @return List<Long>
     */
    List<Long> selectRecycleArticleIdList(@Param("authorId") Long authorId);

    /**
     * 更新文章信息
     *
     * @param article 文章信息
     */
    void updateArticleById(@Param("a") Article article);

    /**
     * 更新文章状态
     *
     * @param articleId 文章id
     * @param status    文章状态
     */
    void updateArticleStatus(@Param("articleId") Long articleId, @Param("status") Integer status);

    /**
     * 删除文章
     *
     * @param articleId  文章id
     * @param deleteType 删除类型
     */
    void deleteArticle(@Param("articleId") Long articleId, @Param("deleteType") Integer deleteType);

    /**
     * 删除文章（清空回收站）
     *
     * @param authorId 作者id
     */
    void deleteArticle3(@Param("authorId") Long authorId);

    /**
     * 恢复文章
     *
     * @param articleId 文章id
     * @return int 影响行数
     */
    int resetArticle(@Param("articleId") Long articleId);
}
