package com.github.stazxr.zblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.domain.dto.ArticleCategoryDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleCategoryQueryDto;
import com.github.stazxr.zblog.domain.entity.ArticleCategory;
import com.github.stazxr.zblog.domain.vo.ArticleCategoryVo;

import java.util.List;

/**
 * 文章类别服务层
 *
 * @author SunTao
 * @since 2021-01-17
 */
public interface ArticleCategoryService extends IService<ArticleCategory> {
    /**
     * 查询类别列表（树）
     *
     * @param queryDto 查询参数
     * @return CategoryVoList
     */
    List<ArticleCategoryVo> queryCategoryTreeList(ArticleCategoryQueryDto queryDto);

    /**
     * 查询一级类别列表
     *
     * @param queryDto 查询参数
     * @return CategoryVoList
     */
    List<ArticleCategoryVo> queryFirstCategoryList(ArticleCategoryQueryDto queryDto);

    /**
     * 查询类别详情
     *
     * @param categoryId 类别ID
     * @return CategoryVo
     */
    ArticleCategoryVo queryCategoryDetail(Long categoryId);

    /**
     * 新增类别
     *
     * @param categoryDto 类别信息
     */
    void addCategory(ArticleCategoryDto categoryDto);

    /**
     * 编辑类别
     *
     * @param categoryDto 类别信息
     */
    void editCategory(ArticleCategoryDto categoryDto);

    /**
     * 删除类别
     *
     * @param categoryId 类别ID
     */
    void deleteCategory(Long categoryId);
}
