package com.github.stazxr.zblog.portal.domain.dto;

import com.github.stazxr.zblog.core.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 友链申请信息
 *
 * @author SunTao
 * @since 2026-08-25
 */
@Getter
@Setter
@ApiModel("友链申请DTO")
public class ApplyFriendLinkDto extends BaseDto {
    private static final long serialVersionUID = -3329702323176134236L;

    /**
     * 网站名称
     */
    @NotBlank(message = "{FRIEND_LINK_NAME_REQUIRED}")
    @ApiModelProperty("网站名称")
    private String name;

    /**
     * 网站地址
     */
    @NotBlank(message = "{FRIEND_LINK_URL_REQUIRED}")
    @ApiModelProperty("网站地址")
    private String url;

    /**
     * 网站Logo
     */
    @ApiModelProperty("网站Logo")
    private String logo;

    /**
     * 网站描述
     */
    @ApiModelProperty("网站描述")
    private String description;

    /**
     * 申请人邮箱
     */
    @ApiModelProperty("申请人邮箱")
    private String email;

    /**
     * 申请人联系方式
     */
    @ApiModelProperty("申请人邮箱")
    private String contact;
}
