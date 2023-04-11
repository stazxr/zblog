package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.core.base.BaseMapper;
import com.github.stazxr.zblog.domain.dto.query.ArticleColumnQueryDto;
import com.github.stazxr.zblog.domain.entity.ArticleCategory;
import com.github.stazxr.zblog.domain.entity.ArticleColumn;
import com.github.stazxr.zblog.domain.vo.ArticleColumnVo;
import com.github.stazxr.zblog.domain.vo.ArticleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章栏目持久层
 *
 * @author SunTao
 * @since 2022-11-22
 */
public interface ArticleColumnMapper extends BaseMapper<ArticleColumn> {
    /**
     * 查询栏目列表
     *
     * @param queryDto 查询参数
     * @return ColumnVoList
     */
    List<ArticleColumnVo> selectColumnList(ArticleColumnQueryDto queryDto);

    /**
     * 查询栏目详情
     *
     * @param columnId 栏目ID
     * @return ColumnVo
     */
    ArticleColumnVo selectColumnDetail(@Param("columnId") Long columnId);

    /**
     * 根据栏目名称查询栏目信息
     *
     * @param name 栏目名称
     * @return ColumnVo
     */
    ArticleCategory selectByColumnName(@Param("name") String name);

    /**
     * 查询非专栏对应的文章列表
     *
     * @param queryDto 查询参数
     * @return ArticleVo
     */
    List<ArticleVo> selectArticleListNotColumn(ArticleColumnQueryDto queryDto);
}
