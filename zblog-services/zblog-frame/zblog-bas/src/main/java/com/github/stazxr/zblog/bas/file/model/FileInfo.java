package com.github.stazxr.zblog.bas.file.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 文件信息
 *
 * @author SunTao
 * @since 2022-07-29
 */
@Getter
@Setter
public class FileInfo {
    /**
     * 物理文件id
     */
    private Long fileId;

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
    private long fileSize;

    /**
     * 文件MD5值
     */
    private String fileMd5;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件后缀
     */
    private String fileSuffix;

    /**
     * 存储绝对路径
     */
    private String fileAbsolutePath;

    /**
     * 存储相对路径
     */
    private String fileRelativePath;

    /**
     * 下载地址
     */
    private String downloadUrl;

    /**
     * 上传方式
     */
    private Integer uploadType;
}
