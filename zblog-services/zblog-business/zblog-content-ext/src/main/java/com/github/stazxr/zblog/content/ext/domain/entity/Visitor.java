package com.github.stazxr.zblog.content.ext.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 访客
 *
 * @author SunTao
 * @since 2026-07-19
 */
@Getter
@Setter
@TableName("visitor")
public class Visitor implements Serializable {
    private static final long serialVersionUID = 7534968814280629693L;

    /**
     * 访客id
     */
    @TableId
    private String visitorId;

    /**
     * 用户id
     */
    private Long userId;

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
     * 区县
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
     * 首次访问时间
     */
    private LocalDateTime firstVisitTime;

    /**
     * 最后访问日期
     */
    private LocalDate lastVisitDate;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
