package com.github.stazxr.zblog.base.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户密码修改信息
 *
 * @author SunTao
 * @since 2022-08-02
 */
@Getter
@Setter
@ApiModel("用户密码修改信息")
public class UserUpdatePassDto implements Serializable {
    private static final long serialVersionUID = 4310961030985312848L;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 原密码
     */
    @ApiModelProperty("原密码")
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

    /**
     * 用户改密相关信息
     */
    @ApiModelProperty(hidden = true)
    private String _f;
}
