package com.github.stazxr.zblog.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 友链信息
 *
 * @author SunTao
 * @since 2022-12-07
 */
@Getter
@Setter
@ToString
@ApiModel("友链信息")
public class FriendLinkDto {
    /**
     * 友链id
     */
    @ApiModelProperty("友链id")
    private Long id;

    /**
     * 友链名称
     */
    @ApiModelProperty("友链名称")
    private String name;

    /**
     * 友链头像地址
     */
    @ApiModelProperty("友链头像地址")
    private String headUrl;

    /**
     * 友链地址
     */
    @ApiModelProperty("友链地址")
    private String linkUrl;

    /**
     * 友链介绍
     */
    @ApiModelProperty("友链介绍")
    private String linkRemark;

    /**
     * 友链状态
     */
    @ApiModelProperty("友链状态")
    private Boolean valid;
}
