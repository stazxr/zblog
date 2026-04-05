package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.core.base.BaseMapper;
import com.github.stazxr.zblog.domain.dto.query.CategoryQueryDto;
import com.github.stazxr.zblog.domain.entity.Category;
import com.github.stazxr.zblog.domain.vo.CategoryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分类管理数据层
 *
 * @author SunTao
 * @since 2021-01-17
 */
public interface CategoryMapper extends BaseMapper<Category> {
    /**
     * 查询分类列表
     *
     * @param queryDto 查询参数
     * @return List<CategoryVo>
     */
    List<CategoryVo> selectCategoryList(CategoryQueryDto queryDto);

    /**
     * 查询分类详情
     *
     * @param categoryId 分类id
     * @return CategoryVo
     */
    CategoryVo selectCategoryDetail(@Param("categoryId") Long categoryId);




//
//    /**
//     * 查询类别对应的文章数目
//     *
//     * @param categoryId 类别ID
//     * @return Count
//     */
//    Long selectArticleCountByCategory(@Param("categoryId") Long categoryId);
//
//    /**
//     * 根据类别名称查询类别信息
//     *
//     * @param name 类别名称
//     * @return Category
//     */
//    Category selectByCategoryName(@Param("name") String name);
//
//    /**
//     * 查询前台分类列表
//     *
//     * @return CategoryList
//     */
//    List<CategoryVo> selectWebCategoryList();
//
//    /**
//     * 获取分类专栏列表
//     *
//     * @return CategoryVos
//     */
//    List<CategoryVo> selectWebBoardCategoryList();
}
