package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.service.ServerMonitorService;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.log.annotation.IgnoredLog;
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
public class ServerMonitorController {
	private final ServerMonitorService serverMonitorService;

	/**
	 * 查询服务器信息
	 *
	 * @return ServerData
	 */
	@IgnoredLog
	@GetMapping(value = "/data")
	@Router(name = "查询服务器信息", code = "queryServerData")
	public Result queryServerData() {
		return Result.success().data(serverMonitorService.queryServerData());
	}
}
