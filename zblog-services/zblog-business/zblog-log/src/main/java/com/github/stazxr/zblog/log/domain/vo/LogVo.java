package com.github.stazxr.zblog.log.domain.vo;

import io.swagger.annotations.ApiModelProperty;
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
     * 日志id
     */
    @ApiModelProperty(value = "日志id")
    private Long id;

    /**
     * TraceId
     */
    @ApiModelProperty(value = "TraceId")
    private String traceId;

    /**
     * 日志类型
     */
    @ApiModelProperty(value = "日志类型")
    private Integer logType;

    /**
     * 操作用户
     */
    @ApiModelProperty(value = "操作用户")
    private String operateUser;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private String eventTime;

    /**
     * 操作描述
     */
    @ApiModelProperty(value = "操作描述")
    private String description;

    /**
     * 操作参数
     */
    @ApiModelProperty(value = "操作参数")
    private String requestParam;

    /**
     * 返回结果
     */
    @ApiModelProperty(value = "返回结果")
    private String requestResult;

    /**
     * 接口编码
     */
    @ApiModelProperty(value = "接口编码")
    private String interfaceCode;

    /**
     * 请求IP
     */
    @ApiModelProperty(value = "请求IP")
    private String requestIp;

    /**
     * 请求URI
     */
    @ApiModelProperty(value = "请求URI")
    private String requestUri;

    /**
     * 请求Method
     */
    @ApiModelProperty(value = "请求Method")
    private String requestMethod;

    /**
     * 请求地址
     */
    @ApiModelProperty(value = "请求地址")
    private String address;

    /**
     * 浏览器
     */
    @ApiModelProperty(value = "浏览器")
    private String browser;

    /**
     * 浏览器版本
     */
    @ApiModelProperty(value = "浏览器版本")
    private String browserVersion;

    /**
     * 平台
     */
    @ApiModelProperty(value = "平台")
    private String platform;

    /**
     * 用户代理
     */
    @ApiModelProperty(value = "用户代理")
    private String userAgent;

    /**
     * 请求耗时
     */
    @ApiModelProperty(value = "请求耗时")
    private Long costTime;

    /**
     * 执行结果
     */
    @ApiModelProperty(value = "执行结果")
    private boolean execResult;

    /**
     * 执行信息
     */
    @ApiModelProperty(value = "执行信息")
    private String execMessage;

    /**
     * 错误码
     */
    @ApiModelProperty(value = "错误码")
    private String errorCode;

    /**
     * 创建日期
     */
    @ApiModelProperty(value = "创建日期")
    private String createDate;
}
