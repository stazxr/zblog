package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.base.domain.bo.HostData;
import com.github.stazxr.zblog.base.service.ServerMonitorService;
import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.log.annotation.IgnoredLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 主机监控
 *
 * @author SunTao
 * @since 2021-09-05
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/host")
@Api(value = "HostMonitorController", tags = { "主机监控" })
public class HostMonitorController {
	private final ServerMonitorService serverMonitorService;

	/**
	 * 查询主机信息
	 *
	 * @return ServerData
	 */
	@IgnoredLog
	@GetMapping(value = "/data")
	@ApiOperation("查询主机信息")
	@ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
	@Router(name = "查询主机信息", code = "HOSTQ001")
	public HostData queryServerData() {
		return serverMonitorService.queryServerData();
	}
}
