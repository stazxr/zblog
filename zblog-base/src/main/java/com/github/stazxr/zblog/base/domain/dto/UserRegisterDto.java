package com.github.stazxr.zblog.base.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户注册信息
 *
 * @author SunTao
 * @since 2023-02-05
 */
@Getter
@Setter
@ToString
@ApiModel("用户注册信息")
public class UserRegisterDto {
    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 验证码
     */
    @ApiModelProperty("验证码")
    private String code;

    /**
     * 缓存标识
     */
    @ApiModelProperty("缓存标识")
    private String uuid;
}
