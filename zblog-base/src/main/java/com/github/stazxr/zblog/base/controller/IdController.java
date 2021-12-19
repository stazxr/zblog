package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.util.GenerateIdUtils;
import com.github.stazxr.zblog.core.model.Result;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ID生成管理器
 *
 * @author SunTao
 * @since 2021-12-19
 */
@RestController
@Api(tags = "系统：序列生成")
@RequestMapping("/api/id")
public class IdController {
    /**
     * 生成唯一ID
     *
     * @return Result
     */
    @GetMapping("/getId")
    @ApiOperation("生成唯一序列")
    public Result getId() {
        return Result.success().data(GenerateIdUtils.getId());
    }

    /**
     * 生成ID列表
     *
     * @return Result
     */
    @ApiOperation("生成序列列表")
    @ApiImplicitParams(
        @ApiImplicitParam(name = "count", value = "获取序列的个数", dataType = "int", paramType = "path")
    )
    @GetMapping("/getIds/{count}")
    public Result getIds(@PathVariable("count") Integer count) {
        return Result.success().data(GenerateIdUtils.getIdList(count));
    }
}
