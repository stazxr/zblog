package com.github.stazxr.zblog.portal.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 评论用户VO
 *
 * @author SunTao
 * @since 2026-09-07
 */
@Getter
@Setter
public class PortalUserVo implements Serializable {
    private static final long serialVersionUID = 5542322220751629988L;

    /**
     * 用户标识
     *
     * 登录用户：userId 转字符串
     * 访客：visitorId
     */
    @ApiModelProperty("用户标识")
    private String id;

    /**
     * 昵称
     */
    @ApiModelProperty("用户昵称")
    private String nickname;

    /**
     * 头像
     */
    @ApiModelProperty("用户头像")
    private String avatar;

    /**
     * 用户网站
     */
    @ApiModelProperty("用户网站")
    private String website;

    /**
     * 是否访客
     */
    @ApiModelProperty("是否访客")
    private Integer userType;
}
