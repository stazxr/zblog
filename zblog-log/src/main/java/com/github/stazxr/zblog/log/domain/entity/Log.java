package com.github.stazxr.zblog.log.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import com.github.stazxr.zblog.log.domain.enums.LogType;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统日志
 *
 * @author SunTao
 * @since 2021-05-16
 */
@Getter
@Setter
@TableName("log")
public class Log extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 日志类型
     */
    private LogType logType;

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
     * 操作方法
     */
    private String operateMethod;

    /**
     * 操作参数
     */
    private String operateParam;

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
     * 异常信息
     */
    private byte[] exceptionDetail;

    public Log(LogType logType, String eventTime, Long costTime) {
        this.logType = logType;
        this.eventTime = eventTime;
        this.costTime = costTime;
    }
}
