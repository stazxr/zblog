package com.github.stazxr.zblog.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.content.domain.dto.query.ArticleAutoSaveQueryDto;
import com.github.stazxr.zblog.content.domain.entity.ArticleContentDraftRecord;
import com.github.stazxr.zblog.content.domain.vo.ArticleAutoSaveVo;
import org.apache.ibatis.annotations.Param;

/**
 * 文章内容草稿数据层
 *
 * @author SunTao
 * @since 2026-10-12
 */
public interface ArticleContentDraftRecordMapper extends BaseMapper<ArticleContentDraftRecord> {
    /**
     * 分页查询文章内容自动保存列表
     *
     * @param page     分页参数
     * @param queryDto 查询参数
     * @return IPage<ArticleAutoSaveVo>
     */
    IPage<ArticleAutoSaveVo> selectAutoSaveArticleContentList(@Param("page") Page<ArticleAutoSaveVo> page, @Param("query") ArticleAutoSaveQueryDto queryDto);

    /**
     * 查询文章内容自动保存详情
     *
     * @param recordId 保存记录id
     * @return ArticleAutoSaveVo
     */
    ArticleAutoSaveVo selectAutoSaveArticleContentById(@Param("recordId") Long recordId);

    /**
     * 根据文章id删除临时保存记录
     *
     * @param articleId 文章id
     */
    void deleteByArticleId(@Param("articleId") Long articleId);
}
