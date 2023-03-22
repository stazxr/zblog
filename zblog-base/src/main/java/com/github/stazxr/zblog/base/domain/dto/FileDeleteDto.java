package com.github.stazxr.zblog.base.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件删除参数信息
 *
 * @author SunTao
 * @since 2022-12-02
 */
@Data
@ApiModel("文件删除参数")
public class FileDeleteDto {
    /**
     * 文件id
     */
    @ApiModelProperty("文件id")
    private Long fileId;

    /**
     * 业务id
     */
    @ApiModelProperty("业务id")
    private Long businessId;
}
