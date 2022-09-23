package com.github.stazxr.zblog.log.domain.vo;

import lombok.Data;

/**
 * 日志信息
 *
 * @author SunTao
 * @since 2022-08-07
 */
@Data
public class LogVo {
    /**
     * 日志编号
     */
    private Long id;

    /**
     * 日志类型：1-操作日志；2-接口日志
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
     * 请求参数
     */
    private String requestParam;

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
     * 执行信息
     */
    private String execMessage;
}
