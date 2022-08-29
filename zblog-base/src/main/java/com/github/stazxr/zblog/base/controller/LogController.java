package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.log.domain.dto.LogQueryDto;
import com.github.stazxr.zblog.log.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志管理
 *
 * @author SunTao
 * @since 2022-08-04
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logs")
public class LogController {
    private final LogService logService;

    /**
     * 查询用户操作日志列表
     *
     * @param queryDto 查询参数
     * @return userLog
     */
    @GetMapping("/queryUserLog")
    @Router(name = "查询用户操作日志列表", code = "queryUserLog", level = BaseConst.PermLevel.PUBLIC)
    public Result queryUserLog(LogQueryDto queryDto) {
        return Result.success().data(logService.queryUserLog(queryDto));
    }

    /**
     * 查询权限的操作日志列表
     *
     * @param queryDto 查询参数
     * @return permLog
     */
    @GetMapping("/queryPermLog")
    @Router(name = "查询权限的操作日志列表", code = "queryPermLog", level = BaseConst.PermLevel.PUBLIC)
    public Result queryPermLog(LogQueryDto queryDto) {
        return Result.success().data(logService.queryPermLog(queryDto));
    }
}
