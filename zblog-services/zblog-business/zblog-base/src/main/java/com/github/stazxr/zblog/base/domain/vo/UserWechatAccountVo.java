package com.github.stazxr.zblog.base.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户微信公众号信息
 *
 * @author SunTao
 * @since 2026-10-31
 */
@Getter
@Setter
@ApiModel("用户微信公众号信息")
public class UserWechatAccountVo implements Serializable {
    private static final long serialVersionUID = 4985514629856818136L;

    /**
     * 配置id
     */
    @ApiModelProperty("配置id")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 微信公众号名称
     */
    @ApiModelProperty("微信公众号名称")
    private String accountName;

    /**
     * 微信公众号AppID
     */
    @ApiModelProperty("微信公众号AppID")
    private String appId;

    /**
     * 微信公众号AppSecret密文
     */
    @ApiModelProperty("微信公众号AppSecret密文")
    private String appSecret;

    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用")
    private Boolean enabled;

    /**
     * 授权验证状态
     */
    @ApiModelProperty("授权验证状态")
    private Integer verifyStatus;

    /**
     * 验证时间
     */
    @ApiModelProperty("验证时间")
    private LocalDateTime verifyTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
