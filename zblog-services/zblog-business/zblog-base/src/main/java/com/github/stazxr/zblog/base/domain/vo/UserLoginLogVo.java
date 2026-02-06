package com.github.stazxr.zblog.base.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.stazxr.zblog.core.base.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 用户登录日志
 *
 * @author SunTao
 * @since 2026-01-23
 */
@Getter
@Setter
@ApiModel("用户登录日志VO")
public class UserLoginLogVo extends BaseVo {
    private static final long serialVersionUID = -6011107531417391695L;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 登录时间
     */
    @ApiModelProperty("登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginTime;

    /**
     * 登录渠道, 01:移动端; 02:PC端
     */
    @ApiModelProperty("登录渠道")
    private String loginChan;

    /**
     * 登录类型, 00:访客登录; 01:密码登录; 02:QQ登录; 99:未知
     */
    @ApiModelProperty("登录类型")
    private String loginType;

    /**
     * 登录IP
     */
    @ApiModelProperty("登录IP")
    private String loginIp;

    /**
     * 登录地址
     */
    @ApiModelProperty("登录地址")
    private String loginAddress;

    /**
     * 浏览器
     */
    @ApiModelProperty("浏览器")
    private String loginBrowser;

    /**
     * 浏览器版本
     */
    @ApiModelProperty("浏览器版本")
    private String loginBrowserVersion;

    /**
     * 登录平台
     */
    @ApiModelProperty("登录平台")
    private String loginPlatform;

    /**
     * 用户代理
     */
    @ApiModelProperty("用户代理")
    private String userAgent;

    /**
     * 登录是否成功
     */
    @ApiModelProperty("登录是否成功")
    private Boolean isSuccess;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
}
