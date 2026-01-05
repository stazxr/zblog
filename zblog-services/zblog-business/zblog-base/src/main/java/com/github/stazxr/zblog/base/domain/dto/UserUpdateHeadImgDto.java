package com.github.stazxr.zblog.base.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户头像修改信息
 *
 * @author SunTao
 * @since 2026-01-01
 */
@Getter
@Setter
@ApiModel("用户头像修改信息")
public class UserUpdateHeadImgDto {
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 文件id
     */
    @ApiModelProperty("文件id")
    private Long fileId;

    /**
     * 头像地址
     */
    @ApiModelProperty("头像地址")
    private String headImg;
}
