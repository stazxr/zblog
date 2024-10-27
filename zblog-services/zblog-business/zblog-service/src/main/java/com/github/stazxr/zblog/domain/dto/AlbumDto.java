package com.github.stazxr.zblog.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 相册信息
 *
 * @author SunTao
 * @since 2022-12-15
 */
@Getter
@Setter
@ToString
@ApiModel("相册信息")
public class AlbumDto {
    /**
     * 相册id
     */
    @ApiModelProperty("相册id")
    private Long id;

    /**
     * 相册名称
     */
    @ApiModelProperty("相册名称")
    private String albumName;

    /**
     * 相册描述
     */
    @ApiModelProperty("相册描述")
    private String albumDesc;

    /**
     * 相册封面
     */
    @ApiModelProperty("相册封面")
    private String albumCover;

    /**
     * 相册状态: {@link com.github.stazxr.zblog.domain.enums.AlbumStatus}
     */
    @ApiModelProperty("相册状态，1：公开、2：私密")
    private Integer status;
}
