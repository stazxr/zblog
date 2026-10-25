package com.github.stazxr.zblog.content.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.content.domain.dto.ArticleAutoSaveDto;
import com.github.stazxr.zblog.content.domain.dto.ArticleDraftDto;
import com.github.stazxr.zblog.content.domain.dto.ArticleDto;
import com.github.stazxr.zblog.content.domain.dto.query.ArticleAutoSaveQueryDto;
import com.github.stazxr.zblog.content.domain.dto.query.ArticleQueryDto;
import com.github.stazxr.zblog.content.domain.entity.Article;
import com.github.stazxr.zblog.content.domain.vo.ArticleAutoSaveVo;
import com.github.stazxr.zblog.content.domain.vo.ArticleCountVo;
import com.github.stazxr.zblog.content.domain.vo.ArticleVo;

/**
 * 文章管理业务层
 *
 * @author SunTao
 * @since 2021-02-23
 */
public interface ArticleService extends IService<Article> {
    /**
     * 分页查询我的文章列表
     *
     * @param queryDto 查询参数
     * @return IPage<ArticleVo>
     */
    IPage<ArticleVo> queryMyArticleListByPage(ArticleQueryDto queryDto);

    /**
     * 查询我的文章数量统计信息
     *
     * @return ArticleCountVo
     */
    ArticleCountVo queryMyArticleCountInfo();

    /**
     * 查询我的文章详情
     *
     * @param articleId 文章id
     * @return ArticleVo
     */
    ArticleVo queryMyArticleDetail(Long articleId);

    /**
     * 初始化文章
     */
    ArticleVo initArticle();

    /**
     * 保存文章
     *
     * @param articleDto 文章信息
     */
    void saveArticle(ArticleDraftDto articleDto);

    /**
     * 发布文章
     *
     * @param articleDto 文章信息
     */
    void publishArticle(ArticleDto articleDto);

    /**
     * 定时发布文章
     *
     * @param articleDto 文章信息
     */
    void autoPublishArticle(ArticleDto articleDto);

    /**
     * 删除文章（移动到回收站）
     *
     * @param articleId 文章id
     */
    void deleteArticle(Long articleId);

    /**
     * 删除文章（彻底删除）
     *
     * @param articleId 文章id
     */
    void deleteArticle2(Long articleId);

    /**
     * 删除文章（清空回收站）
     */
    void deleteArticle3();

    /**
     * 还原文章（移至草稿箱）
     *
     * @param articleId 文章id
     */
    void resetArticle(Long articleId);

    /**
     * 文章内容自动保存
     *
     * @param autoSaveDto 文章自动保存信息
     * @return 自动保存时间
     */
    String autoSaveArticleContent(ArticleAutoSaveDto autoSaveDto);

    /**
     * 分页查询文章内容自动保存列表
     *
     * @param queryDto 查询参数
     * @return IPage<ArticleAutoSaveVo>
     */
    IPage<ArticleAutoSaveVo> queryAutoSaveArticleContentByPage(ArticleAutoSaveQueryDto queryDto);

    /**
     * 查询文章内容自动保存详情
     *
     * @param recordId 保存记录id
     * @return ArticleAutoSaveVo
     */
    ArticleAutoSaveVo queryAutoSaveArticleContentById(Long recordId);

    /**
     * 查询最新的文章草稿
     *
     * @return ArticleVo
     */
    ArticleVo queryLastArticleDraft();

//    /**
//     * 分页查询用户的文章列表
//     *
//     * @param queryDto 查询参数
//     * @return ArticlePageData
//     */
//    ArticlePageData queryArticleListByPage(ArticleQueryDto queryDto);
//
//    /**
//     * 分页查询可见的文章列表
//     *
//     * @param queryDto 查询参数
//     * @return ArticlePageData
//     */
//    ArticlePageData queryPublicArticleListByPage(ArticleQueryDto queryDto);
//
//    /**
//     * 文章定时发布
//     *
//     * @param articleDto 文章信息
//     */
//    void publishArticleByTiming(ArticleDto articleDto);
//    /**
//     * 彻底删除文章
//     *
//     * @param articleId 文章ID
//     */
//    void deleteArticle(Long articleId);
//
//    /**
//     * 批量删除文章
//     *
//     * @param articleIds 文章编号列表
//     */
//    void batchDeleteArticle(List<Long> articleIds);
//
//    /**
//     * 清空回收站
//     */
//    void clearArticleRecycleBin();
//
//    /**
//     * 将文章回收至草稿箱
//     *
//     * @param articleId 文章ID
//     */
//    void recycleToDraftBox(Long articleId);
//
//    /**
//     * 审核文章
//     *
//     * @param auditDto 审核信息
//     */
//    void auditArticle(ArticleAuditDto auditDto);
}
