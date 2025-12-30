package com.github.stazxr.zblog.base.service;

import com.github.stazxr.zblog.base.domain.bo.HostData;

/**
 * 服务监控业务层
 *
 * @author SunTao
 * @since 2021-09-05
 */
public interface ServerMonitorService {
	/**
	 * 查询服务器信息
	 *
	 * @return ServerInfo
	 */
	HostData queryServerData();
}
