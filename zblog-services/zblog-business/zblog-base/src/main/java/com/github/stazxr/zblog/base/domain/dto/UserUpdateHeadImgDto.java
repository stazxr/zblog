package com.github.stazxr.zblog.base.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户头像修改信息
 *
 * @author SunTao
 * @since 2026-01-01
 */
@Getter
@Setter
@ApiModel("用户头像修改信息")
public class UserUpdateHeadImgDto implements Serializable {
    private static final long serialVersionUID = -2874697954720522878L;

    /**
     * 用户id
     */
    @NotNull(message = "{valid.usercenter.userId.NotNull}")
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 文件id
     */
    @NotNull(message = "{valid.usercenter.image.fileIdNotNull}")
    @ApiModelProperty("文件id")
    private Long fileId;
}
