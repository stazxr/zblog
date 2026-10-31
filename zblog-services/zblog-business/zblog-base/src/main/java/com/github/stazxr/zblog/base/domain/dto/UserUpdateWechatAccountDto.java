package com.github.stazxr.zblog.base.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户个人修改微信公众号信息
 *
 * @author SunTao
 * @since 2026-10-30
 */
@Getter
@Setter
@ApiModel("用户个人修改微信公众号信息")
public class UserUpdateWechatAccountDto implements Serializable {
    private static final long serialVersionUID = -3592828589721792065L;

    /**
     * 微信公众号名称
     */
    @NotBlank(message = "{USERCENTER_WECHAT_ACCOUNT_NAME_REQUIRED}")
    @ApiModelProperty("微信公众号名称")
    private String accountName;

    /**
     * 微信公众号AppID
     */
    @NotBlank(message = "{USERCENTER_WECHAT_APP_ID_REQUIRED}")
    @ApiModelProperty("微信公众号AppID")
    private String appId;

    /**
     * 微信公众号AppSecret密文
     */
    @NotNull(message = "{USERCENTER_WECHAT_APP_SECRET_REQUIRED}")
    @ApiModelProperty("微信公众号AppSecret密文")
    private String appSecret;

    /**
     * 是否启用
     */
    @NotNull(message = "{USERCENTER_WECHAT_ENABLED_REQUIRED}")
    @ApiModelProperty("是否启用")
    private Boolean enabled;
}
