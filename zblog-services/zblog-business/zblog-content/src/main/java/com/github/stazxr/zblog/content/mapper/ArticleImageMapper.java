package com.github.stazxr.zblog.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.stazxr.zblog.content.domain.entity.ArticleImageRelation;
import org.apache.ibatis.annotations.Param;

/**
 * 文章封面数据层
 *
 * @author SunTao
 * @since 2026-10-11
 */
public interface ArticleImageMapper extends BaseMapper<ArticleImageRelation> {
    /**
     * 根据文章序列删除中间数据
     *
     * @param articleId 文章序列
     */
    void deleteByArticleId(@Param("articleId") Long articleId);
}
