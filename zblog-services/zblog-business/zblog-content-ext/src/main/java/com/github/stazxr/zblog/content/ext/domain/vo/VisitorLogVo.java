package com.github.stazxr.zblog.content.ext.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 访客日志信息
 *
 * @author SunTao
 * @since 2026-07-21
 */
@Getter
@Setter
@ApiModel("访客日志VO")
public class VisitorLogVo implements Serializable {
    private static final long serialVersionUID = 8130624567554319755L;

    /**
     * 访客日志id
     */
    @ApiModelProperty("访客日志id")
    private Long id;

    /**
     * 访客id
     */
    @ApiModelProperty("访客id")
    private String visitorId;

    /**
     * 访客昵称
     */
    @ApiModelProperty("访客昵称")
    private String visitorNickname;

    /**
     * 访客头像
     */
    @ApiModelProperty("访客头像")
    private String visitorAvatar;

    /**
     * 登录用户id
     */
    @ApiModelProperty("登录用户id")
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 访问路径
     */
    @ApiModelProperty("访问路径")
    private String path;

    /**
     * 页面标题
     */
    @ApiModelProperty("页面标题")
    private String title;

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private String referer;

    /**
     * 访问类型
     */
    @ApiModelProperty("访问类型")
    private String type;

    /**
     * 访问IP
     */
    @ApiModelProperty("访问IP")
    private String ip;

    /**
     * IP归属地
     */
    @ApiModelProperty("IP归属地")
    private String ipRegion;

    /**
     * IP归属地-国家
     */
    @ApiModelProperty("IP归属地-国家")
    private String country;

    /**
     * IP归属地-省份
     */
    @ApiModelProperty("IP归属地-省份")
    private String province;

    /**
     * IP归属地-城市
     */
    @ApiModelProperty("IP归属地-城市")
    private String city;

    /**
     * IP归属地-区县
     */
    @ApiModelProperty("IP归属地-区县")
    private String district;

    /**
     * IP归属地-运营商
     */
    @ApiModelProperty("IP归属地-运营商")
    private String isp;

    /**
     * 用户代理
     */
    @ApiModelProperty("用户代理")
    private String userAgent;

    /**
     * 浏览器名称
     */
    @ApiModelProperty("浏览器名称")
    private String browser;

    /**
     * 浏览器版本
     */
    @ApiModelProperty("浏览器版本")
    private String browserVersion;

    /**
     * 操作系统
     */
    @ApiModelProperty("操作系统")
    private String os;

    /**
     * 设备类型
     */
    @ApiModelProperty("设备类型")
    private String deviceType;

    /**
     * 访问时间
     */
    @ApiModelProperty("访问时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime visitTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}