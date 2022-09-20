package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.domain.dto.query.DictQueryDto;
import com.github.stazxr.zblog.base.service.DictService;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.model.Result;
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
        return Result.success().data(queryDto);
    }
}
