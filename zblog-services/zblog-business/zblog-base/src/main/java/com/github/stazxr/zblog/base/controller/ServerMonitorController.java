package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.service.ServerMonitorService;
import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.log.annotation.IgnoredLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 服务监控
 *
 * @author SunTao
 * @since 2021-09-05
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/server")
@Api(value = "ServerMonitorController", tags = { "服务监控控制器" })
public class ServerMonitorController {
	private final ServerMonitorService serverMonitorService;

	/**
	 * 查询服务器信息
	 *
	 * @return ServerData
	 */
	@IgnoredLog
	@GetMapping(value = "/data")
	@ApiOperation("查询服务器信息")
	@ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
	@Router(name = "查询服务器信息", code = "queryServerData")
	public Result queryServerData() {
		return Result.success().data(serverMonitorService.queryServerData());
	}
}
