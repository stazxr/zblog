package com.github.stazxr.zblog.base.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户邮箱修改参数
 *
 * @author SunTao
 * @since 2022-08-04
 */
@Data
@ApiModel("用户邮箱修改参数")
public class UserUpdateEmailDto {
    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String pass;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 邮箱验证码
     */
    @ApiModelProperty("邮箱验证码")
    private String code;

    /**
     * 缓存标识
     */
    @ApiModelProperty("缓存标识")
    private String uuid;
}
