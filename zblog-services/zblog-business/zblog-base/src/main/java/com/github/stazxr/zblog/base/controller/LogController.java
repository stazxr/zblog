package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.log.domain.dto.LogQueryDto;
import com.github.stazxr.zblog.log.domain.enums.LogType;
import com.github.stazxr.zblog.log.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
@Api(value = "LogController", tags = { "日志控制器" })
public class LogController {
    private final LogService logService;

    /**
     * 分页查询操作日志列表
     *
     * @param queryDto 查询参数
     * @return logList
     */
    @GetMapping(value = "/queryOperateLogsByPage")
    @ApiOperation(value = "分页查询操作日志列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
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
    @ApiOperation(value = "分页查询接口日志列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
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
    @ApiOperation(value = "查询用户日志列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询用户日志列表", code = "queryUserLog", level = BaseConst.PermLevel.PUBLIC)
    public Result queryUserLog(LogQueryDto queryDto) {
        return Result.success().data(logService.queryUserLogListByPage(queryDto));
    }

    /**
     * 导出操作日志
     *
     * @param queryDto 查询参数
     */
    @Log
    @GetMapping("/exportOperateLog")
    @ApiOperation(value = "导出操作日志")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
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
    @ApiOperation(value = "导出所有的日志")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
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
    @ApiOperation(value = "删除日志列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "logType", value = "日志类型，1：操作日志、2：接口日志、3：异常日志", required = true, dataTypeClass = Integer.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "删除日志列表", code = "deleteLog")
    public Result deleteLog(@RequestParam Integer logType) {
        logService.deleteLog(logType);
        return Result.success();
    }

    /**
     * 查询日志堆栈详情
     *
     * @param logId 日志序列
     * @return LogErrorDetail
     */
    @Log
    @GetMapping("/queryLogErrorDetail")
    @ApiOperation(value = "查询日志堆栈详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "logId", value = "日志id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询日志堆栈详情", code = "queryLogErrorDetail")
    public Result queryUserLog(@RequestParam Long logId) {
        return Result.success().data(logService.queryLogErrorDetail(logId));
    }
}
