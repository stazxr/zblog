package com.github.stazxr.zblog.log.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 日志信息
 *
 * @author SunTao
 * @since 2022-08-07
 */
@Getter
@Setter
public class LogVo {
    /**
     * 日志ID
     */
    private Long id;

    /**
     * 日志类型
     */
    private Integer logType;

    /**
     * 操作用户
     */
    private String operateUser;

    /**
     * 操作时间或异常发生时间
     */
    private String eventTime;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 操作参数
     */
    private String requestParam;

    /**
     * 接口编码
     */
    private String interfaceCode;

    /**
     * 请求IP
     */
    private String requestIp;

    /**
     * 请求URI
     */
    private String requestUri;

    /**
     * 请求Method
     */
    private String requestMethod;

    /**
     * 请求地址
     */
    private String address;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 请求耗时
     */
    private Long costTime;

    /**
     * 执行结果
     */
    private boolean execResult;

    /**
     * 执行信息（错误信息）
     */
    private String execMessage;

    /**
     * 创建日期
     */
    private String createDate;
}
