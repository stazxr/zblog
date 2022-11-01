package com.github.stazxr.zblog.base.component.file.model;

import lombok.Data;

/**
 * 文件信息
 *
 * @author SunTao
 * @since 2022-07-29
 */
@Data
public class FileInfo {
    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 原文件名称
     */
    private String originalFileName;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 文件大小
     */
    private long size;

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
     * 上传类型
     */
    private Integer uploadType;
}
