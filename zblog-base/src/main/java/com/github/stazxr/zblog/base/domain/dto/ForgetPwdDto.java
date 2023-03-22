package com.github.stazxr.zblog.base.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 修改密码信息
 *
 * @author SunTao
 * @since 2023-02-06
 */
@Getter
@Setter
@ToString
@ApiModel("用户密码修改参数-忘记密码")
public class ForgetPwdDto {
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
