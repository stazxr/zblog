package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.domain.dto.DictDto;
import com.github.stazxr.zblog.base.domain.dto.query.DictQueryDto;
import com.github.stazxr.zblog.base.service.DictService;
import com.github.stazxr.zblog.core.annotation.RequestPostSingleParam;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.log.annotation.Log;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 字典管理
 *
 * @author SunTao
 * @since 2021-02-20
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dict")
public class DictController {
    private final DictService dictService;

    /**
     * 分页查询字典列表
     *
     * @param queryDto 查询参数
     * @return dictList
     */
    @GetMapping(value = "/pageList")
    @Router(name = "分页查询字典列表", code = "queryDictListByPage")
    public Result queryDictListByPage(DictQueryDto queryDto) {
        return Result.success().data(dictService.queryDictListByPage(queryDto));
    }

    /**
     * 查询字典子列表
     *
     * @param pid PID
     * @return dictList
     */
    @GetMapping(value = "/queryChildList")
    @Router(name = "查询字典子列表", code = "queryChildList", level = BaseConst.PermLevel.PUBLIC)
    public Result queryChildList(@RequestParam Long pid) {
        return Result.success().data(dictService.queryChildList(pid));
    }

    /**
     * 查询字典详情
     *
     * @param dictId 字典序列
     * @return DictVo
     */
    @GetMapping(value = "/queryDictDetail")
    @Router(name = "查询字典详情", code = "queryDictDetail")
    public Result queryDictDetail(@RequestParam Long dictId) {
        return Result.success().data(dictService.queryDictDetail(dictId));
    }

    /**
     * 查询字典信息
     *
     * @param dictId 字典序列
     * @return DictVo
     */
    @GetMapping(value = "/queryDictInfo")
    @Router(name = "查询字典信息", code = "queryDictInfo", level = BaseConst.PermLevel.PUBLIC)
    public Result queryDictInfo(@RequestParam Long dictId) {
        return Result.success().data(dictService.queryDictDetail(dictId));
    }

    /**
     * 新增字典
     *
     * @param dictDto 字典
     * @return Result
     */
    @Log
    @PostMapping(value = "/addDict")
    @Router(name = "新增字典", code = "addDict")
    public Result addDict(@RequestBody DictDto dictDto) {
        if (dictDto.getId() != null) {
            log.warn("A new dict cannot already have an ID: {}", dictDto.getId());
            return Result.failure("参数错误");
        }

        dictService.addDict(dictDto);
        return Result.success();
    }

    /**
     * 编辑字典
     *
     * @param dictDto 字典
     * @return Result
     */
    @Log
    @PostMapping(value = "/editDict")
    @Router(name = "编辑字典", code = "editDict")
    public Result editDict(@RequestBody DictDto dictDto) {
        if (dictDto.getId() == null) {
            return Result.failure("参数错误，缺失ID");
        }

        dictService.editDict(dictDto);
        return Result.success();
    }

    /**
     * 删除字典
     *
     * @param dictId 字典序列
     * @return Result
     */
    @Log
    @PostMapping(value = "/deleteDict")
    @Router(name = "删除字典", code = "deleteDict")
    public Result deleteDict(@RequestPostSingleParam Long dictId) {
        if (dictId == null) {
            return Result.failure("参数错误，缺失ID");
        }

        dictService.deleteDict(dictId);
        return Result.success();
    }
}
