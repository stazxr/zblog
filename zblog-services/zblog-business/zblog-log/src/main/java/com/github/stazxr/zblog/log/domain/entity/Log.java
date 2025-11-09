package com.github.stazxr.zblog.log.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.util.ToStringUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 日志
 *
 * @author SunTao
 * @since 2021-05-16
 */
@Getter
@Setter
@TableName("log")
public class Log implements Serializable {
    private static final long serialVersionUID = 3244794484450592745L;

    /**
     * 主键
     */
    @TableId
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
    private LocalDateTime eventTime;

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
    private LocalDate createDate;

    /**
     * 异常信息
     */
    private byte[] exceptionDetail;

    @Override
    public String toString() {
        return ToStringUtils.buildString(this);
    }
}
