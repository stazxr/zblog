package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.core.base.BaseMapper;
import com.github.stazxr.zblog.domain.dto.query.ArticleCategoryQueryDto;
import com.github.stazxr.zblog.domain.entity.ArticleCategory;
import com.github.stazxr.zblog.domain.vo.ArticleCategoryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章类别数据持久层
 *
 * @author SunTao
 * @since 2021-01-17
 */
public interface ArticleCategoryMapper extends BaseMapper<ArticleCategory> {
    /**
     * 查询类别列表
     *
     * @param queryDto 查询参数
     * @return CategoryVoList
     */
    List<ArticleCategoryVo> selectCategoryList(ArticleCategoryQueryDto queryDto);

    /**
     * 查询类别详情
     *
     * @param categoryId 类别ID
     * @return CategoryVo
     */
    ArticleCategoryVo selectCategoryDetail(@Param("categoryId") Long categoryId);

    /**
     * 查询类别对应的文章数目
     *
     * @param categoryId 类别ID
     * @return Count
     */
    Long selectArticleCountByCategory(@Param("categoryId") Long categoryId);

    /**
     * 根据类别名称查询类别信息
     *
     * @param name 类别名称
     * @return Category
     */
    ArticleCategory selectByCategoryName(@Param("name") String name);

    /**
     * 查询前台分类列表
     *
     * @return CategoryList
     */
    List<ArticleCategoryVo> selectWebCategoryList();

    /**
     * 获取分类专栏列表
     *
     * @return CategoryVos
     */
    List<ArticleCategoryVo> selectWebBoardCategoryList();
}
