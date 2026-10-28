package com.github.stazxr.zblog.content.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.content.domain.dto.query.ArticleVersionQueryDto;
import com.github.stazxr.zblog.content.domain.entity.ArticleVersion;
import com.github.stazxr.zblog.content.domain.vo.ArticleCoverImageVo;
import com.github.stazxr.zblog.content.domain.vo.ArticleTagNameVo;
import com.github.stazxr.zblog.content.domain.vo.ArticleVersionVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章发布版本管理数据层
 *
 * @author SunTao
 * @since 2026-10-26
 */
public interface ArticleVersionMapper extends BaseMapper<ArticleVersion> {
    /**
     * 分页查询文章发布版本列表
     *
     * @param page 分页参数
     * @param queryDto 查询参数
     * @return IPage<ArticleVersionVo>
     */
    IPage<ArticleVersionVo> selectArticleVersionList(@Param("page") Page<ArticleVersionVo> page, @Param("query") ArticleVersionQueryDto queryDto);

    /**
     * 批量查询文章标签名称列表
     *
     * @param articleVersionIds 文章id列表
     * @return List<ArticleTagNameVo>
     */
    List<ArticleTagNameVo> selectArticleTagNamesByArticleVersionIds(@Param("articleVersionIds") List<Long> articleVersionIds);

    /**
     * 批量查询文章封面列表
     *
     * @param articleVersionIds 文章id列表
     * @return List<ArticleCoverImageVo>
     */
    List<ArticleCoverImageVo> selectArticleCoverImagesByArticleVersionIds(@Param("articleVersionIds") List<Long> articleVersionIds);
}
