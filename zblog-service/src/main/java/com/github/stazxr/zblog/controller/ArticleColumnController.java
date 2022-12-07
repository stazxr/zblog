package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.RequestPostSingleParam;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.domain.dto.ArticleColumnDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleColumnQueryDto;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.service.ArticleColumnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 文章栏目管理
 *
 * @author SunTao
 * @since 2020-11-25
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/columns")
public class ArticleColumnController {
    private final ArticleColumnService articleColumnService;

    /**
     * 查询栏目列表
     *
     * @param queryDto 查询参数
     * @return ColumnVoList
     */
    @GetMapping(value = "/pageList")
    @Router(name = "查询栏目列表", code = "queryColumnListByPage")
    public Result queryColumnListByPage(ArticleColumnQueryDto queryDto) {
        return Result.success().data(articleColumnService.queryColumnListByPage(queryDto));
    }

    /**
     * 查询栏目详情
     *
     * @param columnId 标签ID
     * @return TagVo
     */
    @GetMapping(value = "/queryColumnDetail")
    @Router(name = "查询栏目详情", code = "queryColumnDetail")
    public Result queryColumnDetail(Long columnId) {
        return Result.success().data(articleColumnService.queryColumnDetail(columnId));
    }

    /**
     * 新增栏目
     *
     * @param columnDto 栏目信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/addColumn")
    @Router(name = "新增栏目", code = "addColumn")
    public Result addColumn(@RequestBody ArticleColumnDto columnDto) {
        articleColumnService.addColumn(columnDto);
        return Result.success();
    }

    /**
     * 编辑栏目
     *
     * @param columnDto 栏目信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/editColumn")
    @Router(name = "编辑栏目", code = "editColumn")
    public Result editColumn(@RequestBody ArticleColumnDto columnDto) {
        articleColumnService.editColumn(columnDto);
        return Result.success();
    }

    /**
     * 删除栏目
     *
     * @param columnId 栏目ID
     * @return Result
     */
    @Log
    @PostMapping(value = "/deleteColumn")
    @Router(name = "删除栏目", code = "deleteColumn")
    public Result deleteColumn(@RequestPostSingleParam Long columnId) {
        articleColumnService.deleteColumn(columnId);
        return Result.success();
    }
}
