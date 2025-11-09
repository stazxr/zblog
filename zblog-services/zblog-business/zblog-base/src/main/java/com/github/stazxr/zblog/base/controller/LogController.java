package com.github.stazxr.zblog.base.controller;

import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.log.domain.dto.LogQueryDto;
import com.github.stazxr.zblog.log.domain.enums.LogType;
import com.github.stazxr.zblog.log.domain.vo.LogVo;
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
     * 分页查询接口日志列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<LogVo>
     */
    @GetMapping(value = "/pageInterfaceLogList")
    @ApiOperation(value = "分页查询接口日志列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询接口日志列表", code = "LOGIQ001")
    public PageInfo<LogVo> pageInterfaceLogList(LogQueryDto queryDto) {
        queryDto.setLogType(LogType.INTERFACES.getValue());
        return logService.queryLogListByPage(queryDto);
    }

    /**
     * 分页查询操作日志列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<LogVo>
     */
    @GetMapping(value = "/pageOperationLogList")
    @ApiOperation(value = "分页查询操作日志列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询操作日志列表", code = "LOGOQ001")
    public PageInfo<LogVo> pageOperationLogList(LogQueryDto queryDto) {
        queryDto.setLogType(LogType.OPERATION.getValue());
        return logService.queryLogListByPage(queryDto);
    }

    /**
     * 导出接口日志
     *
     * @param queryDto 查询参数
     * @param response Response
     */
    @Log
    @GetMapping("/exportInterfaceLog")
    @ApiOperation(value = "导出接口日志")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "导出接口日志", code = "LOGIE001")
    public void exportInterfaceLog(LogQueryDto queryDto, HttpServletResponse response) {
        queryDto.setLogType(LogType.INTERFACES.getValue());
        logService.exportLogList(queryDto, response);
    }

    /**
     * 导出操作日志
     *
     * @param queryDto 查询参数
     * @param response Response
     */
    @Log
    @GetMapping("/exportOperationLog")
    @ApiOperation(value = "导出操作日志")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "导出操作日志", code = "LOGOE001")
    public void exportOperationLog(LogQueryDto queryDto, HttpServletResponse response) {
        queryDto.setLogType(LogType.OPERATION.getValue());
        logService.exportLogList(queryDto, response);
    }

    /**
     * 查询接口日志异常堆栈
     *
     * @param logId 日志id
     * @return exceptionDetail
     */
    @Log
    @GetMapping("/queryInterfaceLogExpDetail")
    @ApiOperation(value = "查询接口日志异常堆栈")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "logId", value = "日志id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询接口日志异常堆栈", code = "LOGIQ002")
    public String queryInterfaceLogExpDetail(@RequestParam Long logId) {
        return logService.queryLogExpDetail(logId);
    }

    /**
     * 查询操作日志异常堆栈
     *
     * @param logId 日志id
     * @return exceptionDetail
     */
    @Log
    @GetMapping("/queryOperationLogExpDetail")
    @ApiOperation(value = "查询操作日志异常堆栈")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "logId", value = "日志id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询操作日志异常堆栈", code = "LOGOQ002")
    public String queryOperationLogExpDetail(@RequestParam Long logId) {
        return logService.queryLogExpDetail(logId);
    }

    /**
     * 删除接口日志
     *
     * @param queryDto 查询参数
     */
    @Log
    @PostMapping(value = "/deleteInterfaceLog")
    @ApiOperation(value = "删除接口日志")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "删除接口日志", code = "LOGID001")
    public void deleteInterfaceLog(@RequestBody LogQueryDto queryDto) {
        queryDto.setLogType(LogType.INTERFACES.getValue());
        logService.deleteLog(queryDto);
    }

    /**
     * 删除操作日志
     *
     * @param queryDto 查询参数
     */
    @Log
    @PostMapping(value = "/deleteOperationLog")
    @ApiOperation(value = "删除操作日志")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "删除操作日志", code = "LOGOD001")
    public void deleteOperationLog(@RequestBody LogQueryDto queryDto) {
        queryDto.setLogType(LogType.OPERATION.getValue());
        logService.deleteLog(queryDto);
    }
}
