package com.github.stazxr.zblog.base.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户个人修改信息
 *
 * @author SunTao
 * @since 2022-08-04
 */
@Getter
@Setter
@ApiModel("用户个人修改信息")
public class UserUpdateProfileDto implements Serializable {
    private static final long serialVersionUID = 3383905751403264518L;

    /**
     * 用户id
     */
    @NotNull(message = "{TECH_PARAM_INVALID2}")
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 用户昵称
     */
    @NotBlank(message = "{USERCENTER_PROFILE_NICKNAME_REQUIRED}")
    @ApiModelProperty("用户昵称")
    private String nickname;

    /**
     * 用户性别
     */
    @NotNull(message = "{USERCENTER_PROFILE_GENDER_REQUIRED}")
    @ApiModelProperty("用户性别")
    private Integer gender;

    /**
     * 个人网站
     */
    @ApiModelProperty("个人网站")
    private String website;

    /**
     * 个性签名
     */
    @ApiModelProperty("个性签名")
    private String signature;
}
