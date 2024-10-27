package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.core.base.BaseMapper;
import com.github.stazxr.zblog.domain.bo.ArticleCountData;
import com.github.stazxr.zblog.domain.dto.query.ArticleQueryDto;
import com.github.stazxr.zblog.domain.entity.Article;
import com.github.stazxr.zblog.domain.vo.ArticleTmpContentVo;
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
     * 查询用户文章列表
     *
     * @param queryDto 查询参数
     * @return ArticleVoList
     */
    List<ArticleVo> selectUserArticleList(ArticleQueryDto queryDto);

    /**
     * 查询文章类型数量信息
     *
     * @param userId 用户序列
     * @return ArticleCountData
     */
    ArticleCountData selectUserArticleCountData(@Param("userId") Long userId);

    /**
     * 查询公开的文章列表
     *
     * @param queryDto 查询参数
     * @return ArticleVoList
     */
    List<ArticleVo> selectPublicArticleList(ArticleQueryDto queryDto);

    /**
     * 查询公开的文章类型数量信息
     *
     * @return ArticleCountData
     */
    ArticleCountData selectPublicArticleCountData();

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
    void updateArticleStatus(@Param("articleId") Long articleId, @Param("status") Integer status);

    /**
     * 批量修改文章状态
     *
     * @param articleIds 文章编号列表
     * @param status     文章状态
     * @param desc       备注信息
     */
    void batchUpdateArticleStatus(List<Long> articleIds, @Param("status") Integer status, @Param("desc") String desc);

    /**
     * 修改文章描述
     *
     * @param articleId 文章序列
     * @param desc      文章描述
     */
    void updateArticleDesc(@Param("articleId") Long articleId, @Param("desc") String desc);

    /**
     * 查询前台文章列表
     *
     * @param queryDto 查询参数
     * @return ArticleList
     */
    List<ArticleVo> selectWebArticleList(ArticleQueryDto queryDto);

    /**
     * 查询最新的文章草稿
     *
     * @param authorId 作者
     * @return ArticleVo
     */
    ArticleVo selectLastArticleDraft(@Param("authorId") Long authorId);

    /**
     * 删除文章内容临时记录
     *
     * @param articleId 文章序列
     */
    void deleteArticleTmpContent(@Param("articleId") Long articleId);

    /**
     * 新增文章内容临时记录
     *
     * @param articleTmpContent 内容信息
     */
    void insertArticleTmpContent(ArticleTmpContentVo articleTmpContent);

    /**
     * 清空回收站
     *
     * @param authorId 作者
     */
    void clearArticleRecycleBin(@Param("authorId") Long authorId);

    /**
     * 查询文章信息，允许查询已被逻辑删除的数据
     *
     * @param articleId 文章序列
     * @return Article
     */
    Article selectOneWithDeleted(@Param("articleId") Long articleId);

    /**
     * 分页查询文章内容自动保存列表
     *
     * @param articleId 文章序列
     * @return ArticleTmpContentVoList
     */
    List<ArticleTmpContentVo> selectAutoSaveArticleContentList(@Param("articleId") Long articleId);

    /**
     * 查询文章内容自动保存信息
     *
     * @param recordId 记录序列
     * @return ArticleTmpContentVo
     */
    ArticleTmpContentVo selectAutoSaveArticleContentById(@Param("recordId") Long recordId);

    /**
     * 删除文章自动发布的消息记录
     *
     * @param articleId 文章序列
     */
    void deleteArticlePublishTiming(@Param("articleId") Long articleId);

    /**
     * 新增文章自动发布的消息记录
     *
     * @param id            发布序列
     * @param articleId     文章序列
     * @param publishTime   消息发布时间
     * @param producerTime  消息生产时间
     */
    void insertArticlePublishTiming(@Param("id") Long id, @Param("articleId") Long articleId, @Param("publishTime") String publishTime, @Param("producerTime") String producerTime);

    /**
     * 删除文章自动发布的消息记录
     *
     * @param articleId 文章序列
     * @param desc      失效原因
     */
    void invalidateArticlePublishTiming(@Param("articleId") Long articleId, @Param("desc") String desc);

    /**
     * 删除文章自动发布的消息记录
     *
     * @param articleIds 文章序列
     * @param desc       失效原因
     */
    void batchInvalidateArticlePublishTiming(List<Long> articleIds, @Param("desc") String desc);

    /**
     * 根据定时发布记录查询文章信息
     *
     * @param publishId 发布记录
     * @return ArticleVo
     */
    ArticleVo selectArticleByPublishId(@Param("publishId") String publishId);

    /**
     * 根据作者删除文章
     *
     * @param authorId 作者
     */
    void deleteArticleByAuthor(@Param("authorId") Long authorId);

    /**
     * 查找非待审核状态的文章数量
     *
     * @param articleIds 文章列表
     * @return count
     */
    int checkNoAuditCount(List<Long> articleIds);

    /**
     * 根据状态摘取文章列表
     *
     * @param articleIds 文章列表
     * @param status     文章状态
     * @return 文章 ID 列表
     */
    List<Long> splitAuditArticleIds(List<Long> articleIds, @Param("status") Integer status);

    /**
     * 查询前台归档列表
     *
     * @return ArticleList
     */
    List<ArticleVo> selectArchiveList();

    /**
     * 查找上一篇文章
     *
     * @param articleId 文章ID
     * @return ArticleVo
     */
    ArticleVo selectLastArticle(@Param("articleId") Long articleId);

    /**
     * 查找下一篇文章
     *
     * @param articleId 文章ID
     * @return ArticleVo
     */
    ArticleVo selectNextArticle(@Param("articleId") Long articleId);

    /**
     * 查找推荐文章（按照标签、类别查询）
     *
     * @param articleId 文章ID
     * @return ArticleVos
     */
    List<ArticleVo> selectRecommendList(@Param("articleId") Long articleId);

    /**
     * 查找最新文章
     *
     * @return ArticleVos
     */
    List<ArticleVo> selectNewestList();

    /**
     * 根据关键字搜索文章
     *
     * @param keywords 查询关键字
     * @return ArticleList
     */
    List<ArticleVo> selectArticleByKeywords(@Param("keywords") String keywords);

    /**
     * 获取热门文章列表
     *
     * @return ArticleVos
     */
    List<ArticleVo> selectWebBoardHotArticleList();
}
