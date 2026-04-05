package com.github.stazxr.zblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.domain.dto.CategoryDto;
import com.github.stazxr.zblog.domain.dto.query.CategoryQueryDto;
import com.github.stazxr.zblog.domain.entity.Category;
import com.github.stazxr.zblog.domain.vo.CategoryVo;

import java.util.List;

/**
 * 分类管理业务层
 *
 * @author SunTao
 * @since 2021-01-17
 */
public interface CategoryService extends IService<Category> {
    /**
     * 查询分类树列表
     *
     * @param queryDto 查询参数
     * @return List<CategoryVo>
     */
    List<CategoryVo> queryCategoryTree(CategoryQueryDto queryDto);

    /**
     * 查询一级分类列表
     *
     * @param queryDto 查询参数
     * @return List<CategoryVo>
     */
    List<CategoryVo> queryFirstCategoryList(CategoryQueryDto queryDto);

    /**
     * 查询分类详情
     *
     * @param categoryId 分类id
     * @return CategoryVo
     */
    CategoryVo queryCategoryDetail(Long categoryId);

    /**
     * 新增分类
     *
     * @param categoryDto 分类
     */
    void addCategory(CategoryDto categoryDto);

    /**
     * 编辑分类
     *
     * @param categoryDto 分类
     */
    void editCategory(CategoryDto categoryDto);

    /**
     * 删除分类
     *
     * @param categoryId 分类id
     */
    void deleteCategory(Long categoryId);
}
