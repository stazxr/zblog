package com.github.stazxr.zblog.base.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户邮箱修改信息
 *
 * @author SunTao
 * @since 2022-08-04
 */
@Getter
@Setter
@ApiModel("用户邮箱修改信息")
public class UserUpdateEmailDto implements Serializable {
    private static final long serialVersionUID = 3383905751403264518L;

    /**
     * 邮箱
     */
    @NotBlank(message = "{valid.usercenter.email.email.required}")
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 邮箱验证码
     */
    @NotBlank(message = "{valid.usercenter.email.code.required}")
    @ApiModelProperty("邮箱验证码")
    private String code;

    /**
     * 缓存标识
     */
    @NotBlank(message = "{valid.usercenter.email.uuid.required}")
    @ApiModelProperty("缓存标识")
    private String uuid;
}
