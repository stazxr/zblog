package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.RequestPostSingleParam;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.domain.dto.ArticleCategoryDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleCategoryQueryDto;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.service.ArticleCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 文章类别管理
 *
 * @author SunTao
 * @since 2020-11-19
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class ArticleCategoryController {
    private final ArticleCategoryService articleCategoryService;

    /**
     * 查询类别列表（树）
     *
     * @param queryDto 查询参数
     * @return CategoryVoList
     */
    @GetMapping(value = "/treeList")
    @Router(name = "查询类别列表（树）", code = "queryCategoryTreeList")
    public Result queryCategoryTreeList(ArticleCategoryQueryDto queryDto) {
        return Result.success().data(articleCategoryService.queryCategoryTreeList(queryDto));
    }

    /**
     * 查询一级类别列表
     *
     * @param queryDto 查询参数
     * @return CategoryVoList
     */
    @GetMapping(value = "/queryFirstCategoryList")
    @Router(name = "查询一级类别列表", code = "queryFirstCategoryList", level = BaseConst.PermLevel.PUBLIC)
    public Result queryFirstCategoryList(ArticleCategoryQueryDto queryDto) {
        return Result.success().data(articleCategoryService.queryFirstCategoryList(queryDto));
    }

    /**
     * 查询类别详情
     *
     * @param categoryId 类别ID
     * @return CategoryVo
     */
    @GetMapping(value = "/queryCategoryDetail")
    @Router(name = "查询类别详情", code = "queryCategoryDetail")
    public Result queryCategoryDetail(Long categoryId) {
        return Result.success().data(articleCategoryService.queryCategoryDetail(categoryId));
    }

    /**
     * 新增类别
     *
     * @param categoryDto 类别信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/addCategory")
    @Router(name = "新增类别", code = "addCategory")
    public Result addCategory(@RequestBody ArticleCategoryDto categoryDto) {
        articleCategoryService.addCategory(categoryDto);
        return Result.success();
    }

    /**
     * 编辑类别
     *
     * @param categoryDto 类别信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/editCategory")
    @Router(name = "编辑类别", code = "editCategory")
    public Result editCategory(@RequestBody ArticleCategoryDto categoryDto) {
        articleCategoryService.editCategory(categoryDto);
        return Result.success();
    }

    /**
     * 删除类别
     *
     * @param categoryId 类别ID
     * @return Result
     */
    @Log
    @PostMapping(value = "/deleteCategory")
    @Router(name = "删除类别", code = "deleteCategory")
    public Result deleteCategory(@RequestPostSingleParam Long categoryId) {
        articleCategoryService.deleteCategory(categoryId);
        return Result.success();
    }
}
