package com.github.stazxr.zblog.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.stazxr.zblog.base.domain.vo.UploadFileVo;
import com.github.stazxr.zblog.content.domain.entity.ArticleImageRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
     * @param articleId 文章id
     */
    void deleteByArticleId(@Param("articleId") Long articleId);

    /**
     * 查询文章封面列表
     *
     * @param articleId 文章id
     * @return List<String> 文章封面列表
     */
    List<String> selectArticleCoverImages(@Param("articleId") Long articleId);
}
