package com.github.stazxr.zblog.content.ext.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 访客信息
 *
 * @author SunTao
 * @since 2026-07-21
 */
@Getter
@Setter
@ApiModel("访客VO")
public class VisitorVo implements Serializable {
    private static final long serialVersionUID = 712880280464720802L;

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
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

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
     * 首次访问时间
     */
    @ApiModelProperty("首次访问时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime firstVisitTime;

    /**
     * 最后访问日期
     */
    @ApiModelProperty("最后访问日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate lastVisitDate;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
