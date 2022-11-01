package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
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
public class File extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 原文件名称
     */
    private String originalFilename;

    /**
     * 文件大小
     */
    private String size;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 下载链接
     */
    private String downloadUrl;

    /**
     * MD5
     */
    private String md5;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 存储类型
     */
    private Integer storageType;

    /**
     * 上传类型
     */
    private String uploadType;

    /**
     * 是否有效
     */
    @TableLogic
    private Boolean deleted;
}
