package com.github.stazxr.zblog.base.service;

import com.github.stazxr.zblog.base.domain.bo.HostData;

/**
 * 主机监控业务层
 *
 * @author SunTao
 * @since 2021-09-05
 */
public interface HostMonitorService {
	/**
	 * 查询主机信息
	 *
	 * @return ServerData
	 */
	HostData queryHostData();
}
