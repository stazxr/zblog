package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.domain.dto.ArticleColumnDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleColumnQueryDto;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.service.ArticleColumnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章专栏管理
 *
 * @author SunTao
 * @since 2020-11-25
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/columns")
@Api(value = "ArticleColumnController", tags = { "专栏控制器" })
public class ArticleColumnController {
    private final ArticleColumnService articleColumnService;

    /**
     * 分页查询专栏列表
     *
     * @param queryDto 查询参数
     * @return ColumnVoList
     */
    @GetMapping(value = "/pageColumnList")
    @ApiOperation(value = "分页查询专栏列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = "分页查询专栏列表", code = "pageColumnList")
    public Result pageColumnList(ArticleColumnQueryDto queryDto) {
        return Result.success().data(articleColumnService.queryColumnListByPage(queryDto));
    }

    /**
     * 查询专栏详情
     *
     * @param columnId 专栏id
     * @return ColumnVo
     */
    @GetMapping(value = "/queryColumnDetail")
    @ApiOperation(value = "查询专栏详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "columnId", value = "专栏id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = "查询专栏详情", code = "queryColumnDetail", level = BaseConst.PermLevel.PUBLIC)
    public Result queryColumnDetail(@RequestParam Long columnId) {
        return Result.success().data(articleColumnService.queryColumnDetail(columnId));
    }

    /**
     * 新增专栏
     *
     * @param columnDto 专栏信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/addColumn")
    @ApiOperation(value = "新增专栏")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = "新增专栏", code = "addColumn")
    public Result addColumn(@RequestBody ArticleColumnDto columnDto) {
        articleColumnService.addColumn(columnDto);
        return Result.success();
    }

    /**
     * 编辑专栏
     *
     * @param columnDto 专栏信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/editColumn")
    @ApiOperation(value = "编辑专栏")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = "编辑专栏", code = "editColumn")
    public Result editColumn(@RequestBody ArticleColumnDto columnDto) {
        articleColumnService.editColumn(columnDto);
        return Result.success();
    }

    /**
     * 配置专栏
     *
     * @param columnDto 专栏信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/configColumn")
    @ApiOperation(value = "配置专栏")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = "配置专栏", code = "configColumn")
    public Result configColumn(@RequestBody ArticleColumnDto columnDto) {
        articleColumnService.configColumn(columnDto);
        return Result.success();
    }

    /**
     * 删除专栏
     *
     * @param columnId 专栏id
     * @return Result
     */
    @Log
    @PostMapping(value = "/deleteColumn")
    @ApiOperation(value = "删除专栏")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "columnId", value = "专栏id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = "删除专栏", code = "deleteColumn")
    public Result deleteColumn(@RequestParam Long columnId) {
        articleColumnService.deleteColumn(columnId);
        return Result.success();
    }

    /**
     * 查询非专栏对应的文章列表
     *
     * @param queryDto 查询参数
     * @return ArticleVo
     */
    @PostMapping(value = "/queryArticleListNotColumn")
    @ApiOperation(value = "查询非专栏对应的文章列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "articleTitle", value = "文章标题", dataTypeClass = String.class),
        @ApiImplicitParam(name = "articleIds", value = "文章id列表（不包含的关系）", dataTypeClass = List.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = "查询非专栏对应的文章列表", code = "queryArticleListNotColumn", level = BaseConst.PermLevel.PUBLIC)
    public Result queryArticleListNotColumn(@RequestBody ArticleColumnQueryDto queryDto) {
        return Result.success().data(articleColumnService.queryArticleListNotColumn(queryDto));
    }
}
