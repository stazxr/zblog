package com.github.stazxr.zblog.content.ext.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 友链信息
 *
 * @author SunTao
 * @since 2021-03-16
 */
@Getter
@Setter
@ApiModel("友链VO")
public class FriendLinkVo extends BaseVo {
    private static final long serialVersionUID = 9022736711594848655L;

    /**
     * 友链ID
     */
    @ApiModelProperty("友链ID")
    private Long id;

    /**
     * 网站名称
     */
    @ApiModelProperty("网站名称")
    private String name;

    /**
     * 网站地址
     */
    @ApiModelProperty("网站地址")
    private String url;

    /**
     * 网站Logo
     */
    @ApiModelProperty("网站Logo")
    private String logo;

    /**
     * 网站描述
     */
    @ApiModelProperty("网站描述")
    private String description;

    /**
     * 申请人邮箱
     */
    @ApiModelProperty("申请人邮箱")
    private String email;

    /**
     * 联系方式（QQ/微信等）
     */
    @ApiModelProperty("联系方式（QQ/微信等）")
    private String contact;

    /**
     * 审批状态
     */
    @ApiModelProperty("审批状态")
    private Integer status;

    /**
     * 是否展示
     */
    @ApiModelProperty("是否展示")
    private Boolean isVisible;

    /**
     * 是否允许传递SEO权重
     */
    @ApiModelProperty("是否允许传递SEO权重")
    private Boolean allowFollow;

    /**
     * 排序值（越大越靠前）
     */
    @ApiModelProperty("排序值（越大越靠前）")
    private Integer sort;
}
