package com.github.stazxr.zblog.base.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.stazxr.zblog.util.io.FileUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件信息
 *
 * @author SunTao
 * @since 2022-07-27
 */
@Getter
@Setter
@ApiModel("文件VO")
public class FileVo implements Serializable {
    private static final long serialVersionUID = 6250467959157937827L;

    /**
     * 逻辑文件id
     */
    @ApiModelProperty("逻辑文件id")
    private Long fileId;

    /**
     * 物理文件id
     */
    @ApiModelProperty("物理文件id")
    private Long fileStorageId;

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
     * 创建用户
     */
    @ApiModelProperty("创建用户")
    private Long createUser;

    /**
     * 创建用户名
     */
    @ApiModelProperty("创建用户名")
    private String createUsername;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 文件MD5值
     */
    @ApiModelProperty("文件MD5值")
    private String fileMd5;

    /**
     * 文件大小
     */
    @ApiModelProperty("文件大小")
    private long fileSize;

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

    /**
     * 文件类型
     */
    @ApiModelProperty("文件类型")
    private String fileType;

    /**
     * 上传类型
     */
    @ApiModelProperty("上传类型")
    private Integer uploadType;

    /**
     * 业务ID
     */
    @ApiModelProperty("业务ID")
    private Long businessId;

    /**
     * 业务类型
     */
    @ApiModelProperty("业务类型")
    private Integer businessType;

    /**
     * 物理文件引用次数
     */
    @ApiModelProperty("物理文件引用次数")
    private Integer referenceCount;

    /**
     * 获取文件大小
     *
     * @return 文件大小的友好展示
     */
    public String getFileSize() {
        return FileUtils.convertFileSize(fileSize);
    }
}
