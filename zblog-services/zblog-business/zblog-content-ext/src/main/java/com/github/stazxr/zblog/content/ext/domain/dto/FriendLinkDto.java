package com.github.stazxr.zblog.content.ext.domain.dto;

import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.core.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 友链信息
 *
 * @author SunTao
 * @since 2021-03-16
 */
@Getter
@Setter
@ApiModel("友链DTO")
public class FriendLinkDto extends BaseDto {
    private static final long serialVersionUID = 9022736711594848655L;

    /**
     * 友链id
     */
    @NotNull(groups = Update.class, message = "{TECH_PARAM_MISS}")
    @ApiModelProperty("友链id")
    private Long id;

    /**
     * 网站名称
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{FRIEND_LINK_NAME_REQUIRED}")
    @ApiModelProperty("网站名称")
    private String name;

    /**
     * 网站地址
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{FRIEND_LINK_URL_REQUIRED}")
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
     * 友链类型
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{FRIEND_LINK_TYPE_REQUIRED}")
    @ApiModelProperty("友链类型")
    private Integer linkType;

    /**
     * 申请人邮箱
     */
    @ApiModelProperty("申请人邮箱")
    private String email;

    /**
     * 联系方式（QQ/微信等）
     */
    @ApiModelProperty("联系方式（QQ/微信等）")
    private String contact;

    /**
     * 是否展示
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{FRIEND_LINK_VISIBLE_REQUIRED}")
    @ApiModelProperty("是否展示")
    private Boolean isVisible;

    /**
     * 是否允许传递SEO权重
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{FRIEND_LINK_FOLLOW_REQUIRED}")
    @ApiModelProperty("是否允许传递SEO权重")
    private Boolean allowFollow;

    /**
     * 是否开启健康检测
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{FRIEND_LINK_CHECK_REQUIRED}")
    @ApiModelProperty("是否开启健康检测")
    private Boolean checkEnabled;

    /**
     * 排序值（越大越靠前）
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{PARAM_SORT_REQUIRED}")
    @Min(value = 0, groups = {Create.class, Update.class}, message = "{PARAM_SORT_MIN0}")
    @Max(value = 99999, groups = {Create.class, Update.class}, message = "{PARAM_SORT_MAX99999}")
    @ApiModelProperty("排序值（越大越靠前）")
    private Integer sort;
}
