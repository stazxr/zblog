package com.github.stazxr.zblog.content.ext.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 访客日志
 *
 * @author SunTao
 * @since 2026-07-20
 */
@Getter
@Setter
@TableName("visitor_log")
public class VisitorLog implements Serializable {
    private static final long serialVersionUID = -6545290405396676372L;

    /**
     * 访客日志id
     */
    @TableId
    private Long id;

    /**
     * 访客id
     */
    private String visitorId;

    /**
     * 登录用户id
     */
    private Long userId;

    /**
     * 访问路径
     */
    private String path;

    /**
     * 页面标题
     */
    private String title;

    /**
     * 来源页面
     */
    private String referer;

    /**
     * 访问类型
     */
    private String type;

    /**
     * 访问IP
     */
    private String ip;

    /**
     * IP归属地-国家
     */
    private String country;

    /**
     * IP归属地-省份
     */
    private String province;

    /**
     * IP归属地-城市
     */
    private String city;

    /**
     * IP归属地-区县
     */
    private String district;

    /**
     * IP归属地-运营商
     */
    private String isp;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 浏览器名称
     */
    private String browser;

    /**
     * 浏览器版本
     */
    private String browserVersion;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 访问时间
     */
    private LocalDateTime visitTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}