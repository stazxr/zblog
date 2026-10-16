package com.github.stazxr.zblog.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.stazxr.zblog.content.domain.entity.ArticleTagRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章标签数据层
 *
 * @author SunTao
 * @since 2026-10-10
 */
public interface ArticleTagMapper extends BaseMapper<ArticleTagRelation> {
    /**
     * 根据文章序列删除中间数据
     *
     * @param articleId 文章id
     */
    void deleteByArticleIdSoft(@Param("articleId") Long articleId);

    /**
     * 根据文章序列删除中间数据
     *
     * @param articleId 文章id
     */
    void deleteByArticleIdHard(@Param("articleId") Long articleId);

    /**
     * 根据文章id查询标签id列表
     *
     * @param articleId 文章id
     * @return List<Long> 标签id列表
     */
    List<Long> selectTagIdsByArticleId(@Param("articleId") Long articleId);
}
