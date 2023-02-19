package com.github.stazxr.zblog.log.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import com.github.stazxr.zblog.core.util.IpImplUtils;
import com.github.stazxr.zblog.util.net.IpUtils;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

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
     * 日志类型（操作日志、接口日志）
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

    /**
     * 异常信息
     */
    private byte[] exceptionDetail;

    /**
     * 设置请求相关信息
     *
     * @param request 请求信息
     */
    public void setRequestInfo(HttpServletRequest request) {
        this.requestIp = IpUtils.getIp(request);
        this.requestUri = request.getRequestURI();
        this.requestMethod = request.getMethod();
        this.address = IpImplUtils.getIpSource(this.requestIp);
        this.browser = IpUtils.getBrowser(request);
    }
}
