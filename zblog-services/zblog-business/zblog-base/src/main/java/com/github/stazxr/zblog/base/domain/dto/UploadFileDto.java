package com.github.stazxr.zblog.base.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 上传文件信息
 *
 * @author SunTao
 * @since 2026-01-04
 */
@Getter
@Setter
@ApiModel("上传文件DTO")
public class UploadFileDto implements Serializable {
    private static final long serialVersionUID = -153463720136164190L;

    /**
     * 逻辑文件id
     */
    @ApiModelProperty("逻辑文件id")
    private Long fileId;

    /**
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    private String filename;

    /**
     * 原文件名称
     */
    @ApiModelProperty("原文件名称")
    private String originalFilename;

    /**
     * 存储绝对路径
     */
    @ApiModelProperty("存储绝对路径")
    private String fileAbsolutePath;

    /**
     * 存储相对路径
     */
    @ApiModelProperty("存储相对路径")
    private String fileRelativePath;

    /**
     * 访问地址
     */
    @ApiModelProperty("访问地址")
    private String fileAccessUrl;
}
