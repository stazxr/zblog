package com.github.stazxr.zblog.core.enums;

/**
 * 日志级别
 *
 * @author SunTao
 * @since 2021-05-16
 */
public enum LogLevel {
	/**
	 * 一般，日志入库
	 */
	NORMAL,

	/**
	 * 通告，消息中心提醒
	 */
	NOTICE,

	/**
	 * 通告，消息中心提醒/其他消息通知
	 */
	WARN,

	/**
	 * 风险，其他消息通知
	 */
	RISK
}
