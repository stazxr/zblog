package com.github.stazxr.zblog.base.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * QQ 登录参数
 *
 * @author SunTao
 * @since 2023-06-26
 */
@Getter
@Setter
@ToString
@ApiModel("QQ 登录参数")
public class QqLoginParam implements Serializable {
    private static final long serialVersionUID = 7751421980188107011L;

    /**
     * openId
     */
    @ApiModelProperty(name = "openId", value = "openId", required = true, dataType = "String")
    private String openId;

    /**
     * accessToken
     */
    @ApiModelProperty(name = "accessToken", value = "accessToken", required = true, dataType = "String")
    private String accessToken;
}
