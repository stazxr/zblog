package com.github.stazxr.zblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.domain.dto.ArticleDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleQueryDto;
import com.github.stazxr.zblog.domain.entity.Article;
import com.github.stazxr.zblog.domain.vo.ArticleVo;

/**
 * 文章服务层
 *
 * @author SunTao
 * @since 2021-02-23
 */
public interface ArticleService extends IService<Article> {
    /**
     * 分页查询文章列表
     *
     * @param queryDto 查询参数
     * @return ArticleVoList
     */
    PageInfo<ArticleVo> queryArticleListByPage(ArticleQueryDto queryDto);

    /**
     * 查询文章详情
     *
     * @param articleId 文章ID
     * @return ArticleVo
     */
    ArticleVo queryArticleDetail(Long articleId);

    /**
     * 新增文章
     *
     * @param articleDto 文章信息
     */
    void addArticle(ArticleDto articleDto);

    /**
     * 编辑文章
     *
     * @param articleDto 文章信息
     */
    void editArticle(ArticleDto articleDto);

    /**
     * 删除文章
     *
     * @param articleId 文章ID
     */
    void deleteArticle(Long articleId);
}
