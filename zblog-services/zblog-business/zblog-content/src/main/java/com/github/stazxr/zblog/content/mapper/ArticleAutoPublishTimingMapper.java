package com.github.stazxr.zblog.content.mapper;

import com.github.stazxr.zblog.content.domain.entity.ArticleAutoPublishTiming;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 文章定时发布任务记录数据层
 *
 * @author SunTao
 * @since 2026-10-28
 */
public interface ArticleAutoPublishTimingMapper extends BaseMapper<ArticleAutoPublishTiming> {
    /**
     * 更新文章自动发布任务状态为无效
     *
     * @param articleId 文章id
     * @param remark    原因
     */
    void invalidHistoryRecord(@Param("articleId") Long articleId, @Param("remark") String remark);

    /**
     * 查询文章审核中的发布记录
     *
     * @param articleId 文章id
     * @return ArticleAutoPublishTiming
     */
    ArticleAutoPublishTiming selectReviewRecord(@Param("articleId") Long articleId);

    /**
     * 查询文章待发布的记录
     *
     * @param articleId 文章id
     * @return ArticleAutoPublishTiming
     */
    ArticleAutoPublishTiming selectToPublishRecord(@Param("articleId") Long articleId);

    /**
     * 更新待审核态为待同步态
     *
     * @param articleId 文章id
     */
    void updateReviewToToDoStatus(@Param("articleId") Long articleId);

    /**
     * 更新待审核态为拒绝态
     *
     * @param articleId 文章id
     * @param remark    备注
     */
    void updateReviewToRejectStatus(@Param("articleId") Long articleId, @Param("remark") String remark);
}
