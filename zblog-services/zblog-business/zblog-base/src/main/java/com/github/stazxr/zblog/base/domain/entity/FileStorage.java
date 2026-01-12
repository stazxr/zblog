package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.bas.file.model.FileInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 物理文件
 *
 * @author SunTao
 * @since 2026-01-02
 */
@Getter
@Setter
@TableName("file_storage")
public class FileStorage implements Serializable {
    private static final long serialVersionUID = -547025151029254966L;

    /**
     * 文件id
     */
    @TableId
    private Long id;

    /**
     * 文件名（新生成的）
     */
    private String filename;

    /**
     * 文件MD5值
     */
    private String fileMd5;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 存储绝对路径
     */
    private String fileAbsolutePath;

    /**
     * 存储相对路径
     */
    private String fileRelativePath;

    /**
     * 访问地址
     */
    private String fileAccessUrl;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 上传类型
     */
    private Integer uploadType;

    /**
     * 上传用户，需要记录是谁首次把文件上传到文件服务器的
     */
    private Long uploadUser;

    /**
     * 上传时间
     */
    private Date uploadTime;

    public FileStorage() {
    }

    public FileStorage(FileInfo fileInfo) {
        setId(fileInfo.getFileId());
        setFilename(fileInfo.getFilename());
        setFileMd5(fileInfo.getFileMd5());
        setFileSize(fileInfo.getFileSize());
        setFileAbsolutePath(fileInfo.getStorageLocation());
        setFileRelativePath(fileInfo.getFileRelativePath());
        setFileAccessUrl(fileInfo.getFileAccessUrl());
        setFileType(fileInfo.getFileType());
        setUploadType(fileInfo.getUploadType());
        setUploadTime(new Date());
    }
}
