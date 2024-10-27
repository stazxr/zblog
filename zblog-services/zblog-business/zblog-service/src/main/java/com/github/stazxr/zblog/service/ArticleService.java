package com.github.stazxr.zblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.domain.bo.ArticlePageData;
import com.github.stazxr.zblog.domain.dto.ArticleAuditDto;
import com.github.stazxr.zblog.domain.dto.ArticleDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleQueryDto;
import com.github.stazxr.zblog.domain.entity.Article;
import com.github.stazxr.zblog.domain.vo.ArticleTmpContentVo;
import com.github.stazxr.zblog.domain.vo.ArticleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章服务层
 *
 * @author SunTao
 * @since 2021-02-23
 */
public interface ArticleService extends IService<Article> {
    /**
     * 分页查询用户的文章列表
     *
     * @param queryDto 查询参数
     * @return ArticlePageData
     */
    ArticlePageData queryArticleListByPage(ArticleQueryDto queryDto);

    /**
     * 分页查询可见的文章列表
     *
     * @param queryDto 查询参数
     * @return ArticlePageData
     */
    ArticlePageData queryPublicArticleListByPage(ArticleQueryDto queryDto);

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
     * 保存文章草稿
     *
     * @param articleDto 文章信息
     * @return 保存时间
     */
    String saveArticleDraft(ArticleDto articleDto);

    /**
     * 查询最新的文章草稿
     *
     * @return ArticleVo
     */
    ArticleVo queryLastArticleDraft();

    /**
     * 文章内容自动保存
     *
     * @param articleDto 文章内容信息
     * @return saveTime
     */
    String autoSaveArticleContent(ArticleDto articleDto);

    /**
     * 分页查询文章内容自动保存列表
     *
     * @param queryDto 查询参数
     * @return ArticleTmpContentVoList
     */
    PageInfo<ArticleTmpContentVo> queryAutoSaveArticleContentByPage(ArticleQueryDto queryDto);

    /**
     * 查询文章内容自动保存信息
     *
     * @param recordId 记录序列
     * @return ArticleTmpContentVo
     */
    ArticleTmpContentVo queryAutoSaveArticleContentById(Long recordId);

    /**
     * 文章定时发布
     *
     * @param articleDto 文章信息
     */
    void publishArticleByTiming(ArticleDto articleDto);

    /**
     * 删除文章
     *
     * @param articleId 文章ID
     */
    void moveArticleToRecycleBin(@Param("articleId") Long articleId);

    /**
     * 彻底删除文章
     *
     * @param articleId 文章ID
     */
    void deleteArticle(Long articleId);

    /**
     * 批量删除文章
     *
     * @param articleIds 文章编号列表
     */
    void batchDeleteArticle(List<Long> articleIds);

    /**
     * 清空回收站
     */
    void clearArticleRecycleBin();

    /**
     * 将文章回收至草稿箱
     *
     * @param articleId 文章ID
     */
    void recycleToDraftBox(Long articleId);

    /**
     * 查询文章默认封面
     *
     * @return ArticleImg
     */
    String queryArticleDefaultImg();

    /**
     * 审核文章
     *
     * @param auditDto 审核信息
     */
    void auditArticle(ArticleAuditDto auditDto);
}
