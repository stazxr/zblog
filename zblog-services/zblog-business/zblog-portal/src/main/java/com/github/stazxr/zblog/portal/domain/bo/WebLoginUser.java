package com.github.stazxr.zblog.portal.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Web用户登录信息
 *
 * @author SunTao
 * @since 2027-07-12
 */
@Getter
@Setter
@ApiModel("Web端用户登录信息")
public class WebLoginUser implements Serializable {
    private static final long serialVersionUID = -6089215295004547773L;

    /**
     * 用户是否认证
     */
    @ApiModelProperty("用户是否认证")
    private boolean authenticated;

    /**
     * 用户基础信息
     */
    @ApiModelProperty("用户基础信息")
    private UserBaseInfo user;
}
