package com.github.stazxr.zblog.base.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用户更新字段信息
 *
 * @author SunTao
 * @since 2022-07-31
 */
@Data
@ApiModel("用户信息")
public class UserDto {
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long id;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    private String headImg;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String nickname;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String telephone;

    /**
     * 个性签名
     */
    @ApiModelProperty("个性签名")
    private String signature;

    /**
     * 个人网站
     */
    @ApiModelProperty("个人网站")
    private String website;

    /**
     * 用户性别
     */
    @ApiModelProperty("用户性别")
    private Integer gender;

    /**
     * 用户邮箱
     */
    @ApiModelProperty("用户邮箱")
    private String email;

    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用")
    private Boolean enabled;

    /**
     * 是否临时用户
     */
    @ApiModelProperty("是否临时用户")
    private Boolean temp;

    /**
     * 临时用户有效时间
     */
    @ApiModelProperty("临时用户有效时间")
    private String expiredTime;

    /**
     * 角色id列表
     */
    @ApiModelProperty("角色id列表")
    private List<Long> roleIds;
}
