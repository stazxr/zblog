package com.github.stazxr.zblog.base.domain.vo;

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
@ApiModel("上传文件VO")
public class UploadFileVo implements Serializable {
    private static final long serialVersionUID = 6250467959157937827L;

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
     * 下载地址
     */
    @ApiModelProperty("下载地址")
    private String downloadUrl;
}
