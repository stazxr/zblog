package com.github.stazxr.zblog.content.controller;

import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.content.domain.dto.CategoryDto;
import com.github.stazxr.zblog.content.domain.dto.query.CategoryQueryDto;
import com.github.stazxr.zblog.content.domain.vo.CategoryVo;
import com.github.stazxr.zblog.content.service.CategoryService;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 *
 * @author SunTao
 * @since 2020-11-19
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@Api(value = "CategoryController", tags = { "分类管理" })
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * 查询分类树列表
     *
     * @param queryDto 查询参数
     * @return List<CategoryVo>
     */
    @GetMapping(value = "/queryCategoryTree")
    @ApiOperation(value = "查询分类树列表")
    @ApiVersion(value = BaseConst.ApiVersion.V_4_0_0)
    @Router(name = "查询分类树列表", code = "CATEQ001")
    public List<CategoryVo> queryCategoryTree(CategoryQueryDto queryDto) {
        return categoryService.queryCategoryTree(queryDto);
    }

    /**
     * 查询分类树列表（公共）
     *
     * @return List<CategoryVo>
     */
    @GetMapping(value = "/queryPublicCategoryTree")
    @ApiOperation(value = "查询分类树列表（公共）")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "查询分类树列表（公共）", code = "CATEQ004", level = RouterLevel.PUBLIC)
    public List<CategoryVo> queryPublicCategoryTree() {
        return categoryService.queryCategoryTree(new CategoryQueryDto());
    }

    /**
     * 查询一级分类列表
     *
     * @param queryDto 查询参数
     * @return List<CategoryVo>
     */
    @GetMapping(value = "/queryFirstCategoryList")
    @ApiOperation(value = "查询一级分类列表")
    @ApiVersion(value = BaseConst.ApiVersion.V_4_0_0)
    @Router(name = "查询一级分类列表", code = "CATEQ002", level = RouterLevel.PUBLIC)
    public List<CategoryVo> queryFirstCategoryList(CategoryQueryDto queryDto) {
        return categoryService.queryFirstCategoryList(queryDto);
    }

    /**
     * 查询分类详情
     *
     * @param categoryId 分类id
     * @return CategoryVo
     */
    @GetMapping(value = "/queryCategoryDetail")
    @ApiOperation(value = "查询分类详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "categoryId", value = "分类id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_4_0_0)
    @Router(name = "查询分类详情", code = "CATEQ003")
    public CategoryVo queryCategoryDetail(@RequestParam Long categoryId) {
        return categoryService.queryCategoryDetail(categoryId);
    }

    /**
     * 新增分类
     *
     * @param categoryDto 分类
     */
    @Log
    @PostMapping(value = "/addCategory")
    @ApiOperation(value = "新增分类")
    @ApiVersion(value = BaseConst.ApiVersion.V_4_0_0)
    @Router(name = "新增分类", code = "CATEA001")
    public void addCategory(@RequestBody @Validated(Create.class) CategoryDto categoryDto) {
        categoryService.addCategory(categoryDto);
    }

    /**
     * 编辑分类
     *
     * @param categoryDto 分类
     */
    @Log
    @PostMapping(value = "/editCategory")
    @ApiOperation(value = "编辑分类")
    @ApiVersion(value = BaseConst.ApiVersion.V_4_0_0)
    @Router(name = "编辑分类", code = "CATEU001")
    public void editCategory(@RequestBody @Validated(Update.class) CategoryDto categoryDto) {
        categoryService.editCategory(categoryDto);
    }

    /**
     * 删除分类
     *
     * @param categoryId 分类id
     */
    @Log
    @PostMapping(value = "/deleteCategory")
    @ApiOperation(value = "删除分类")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "categoryId", value = "分类id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_4_0_0)
    @Router(name = "删除分类", code = "CATED001")
    public void deleteCategory(@RequestParam Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}
