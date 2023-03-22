package com.github.stazxr.zblog.base.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户密码修改参数
 *
 * @author SunTao
 * @since 2022-08-02
 */
@Data
@ApiModel("用户密码修改参数")
public class UserUpdatePassDto {
    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 旧密码
     */
    @ApiModelProperty("旧密码")
    private String oldPass;

    /**
     * 新密码
     */
    @ApiModelProperty("新密码")
    private String newPass;

    /**
     * 密码确认
     */
    @ApiModelProperty("密码确认")
    private String confirmPass;
}
