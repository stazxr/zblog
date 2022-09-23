package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.core.annotation.RequestPostSingleParam;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.log.domain.dto.LogQueryDto;
import com.github.stazxr.zblog.log.domain.enums.LogType;
import com.github.stazxr.zblog.log.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

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
     * 分页查询操作日志列表
     *
     * @param queryDto 查询参数
     * @return logList
     */
    @GetMapping(value = "/queryOperateLogsByPage")
    @Router(name = "分页查询操作日志列表", code = "queryOperateLogsByPage")
    public Result queryOperateLogsByPage(LogQueryDto queryDto) {
        queryDto.setLogType(LogType.OPERATE.getValue());
        return Result.success().data(logService.queryLogListByPage(queryDto));
    }

    /**
     * 分页查询接口日志列表
     *
     * @param queryDto 查询参数
     * @return logList
     */
    @GetMapping(value = "/queryApiLogsByPage")
    @Router(name = "分页查询接口日志列表", code = "queryApiLogsByPage")
    public Result queryApiLogsByPage(LogQueryDto queryDto) {
        return Result.success().data(logService.queryLogListByPage(queryDto));
    }

    /**
     * 查询用户日志列表
     *
     * @param queryDto 查询参数
     * @return userLog
     */
    @GetMapping("/queryUserLog")
    @Router(name = "查询用户日志列表", code = "queryUserLog", level = BaseConst.PermLevel.PUBLIC)
    public Result queryUserLog(LogQueryDto queryDto) {
        return Result.success().data(logService.queryUserLog(queryDto));
    }

    /**
     * 导出操作日志
     *
     * @param queryDto 查询参数
     */
    @Log
    @GetMapping("/exportOperateLog")
    @Router(name = "导出操作日志", code = "exportOperateLog")
    public void exportOperateLog(LogQueryDto queryDto, HttpServletResponse response) {
        queryDto.setLogType(LogType.OPERATE.getValue());
        logService.exportLogList(queryDto, response);
    }

    /**
     * 导出所有的日志
     *
     * @param queryDto 查询参数
     */
    @Log
    @GetMapping("/exportAllLog")
    @Router(name = "导出所有的日志", code = "exportAllLog")
    public void exportAllLog(LogQueryDto queryDto, HttpServletResponse response) {
        logService.exportLogList(queryDto, response);
    }

    /**
     * 删除日志列表
     *
     * @param logType 日志类型
     * @return Result
     */
    @Log
    @PostMapping(value = "/deleteLog")
    @Router(name = "删除日志列表", code = "deleteLog")
    public Result deleteLog(@RequestPostSingleParam Integer logType) {
        logService.deleteLog(logType);
        return Result.success();
    }

    /**
     * 查询日志堆栈详情
     *
     * @param logId 日志序列
     * @return LogErrorDetail
     */
    @GetMapping("/queryLogErrorDetail")
    @Router(name = "查询日志堆栈详情", code = "queryLogErrorDetail")
    public Result queryUserLog(Long logId) {
        return Result.success().data(logService.queryLogErrorDetail(logId));
    }
}
