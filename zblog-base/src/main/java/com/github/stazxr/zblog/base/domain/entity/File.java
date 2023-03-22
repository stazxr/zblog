package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 文件表
 *
 * @author SunTao
 * @since 2022-07-27
 */
@Getter
@Setter
@TableName("file")
@ApiModel("文件实体")
public class File extends BaseEntity {
    /**
     * 文件id
     */
    @TableId
    @ApiModelProperty("文件id")
    private Long id;

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
     * 文件大小
     */
    @ApiModelProperty("文件大小")
    private String size;

    /**
     * 文件路径
     */
    @ApiModelProperty("文件路径")
    private String filePath;

    /**
     * 下载链接
     */
    @ApiModelProperty("下载链接")
    private String downloadUrl;

    /**
     * 文件MD5值
     */
    @ApiModelProperty("文件MD5值")
    private String md5;

    /**
     * 文件类型
     */
    @ApiModelProperty("文件类型")
    private String fileType;

    /**
     * 存储类型
     */
    @ApiModelProperty("存储类型，1：本地、2：阿里云、3：七牛云")
    private Integer storageType;

    /**
     * 上传类型
     */
    @ApiModelProperty("上传类型，1：普通上传、2：测试上传")
    private String uploadType;

    /**
     * 是否删除
     */
    @TableLogic
    @ApiModelProperty("是否删除")
    private Boolean deleted;
}
