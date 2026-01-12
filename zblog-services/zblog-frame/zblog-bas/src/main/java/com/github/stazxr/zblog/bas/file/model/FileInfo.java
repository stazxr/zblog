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
     * 物理文件id（保留字段，方便后续扩展其他功能）
     */
    private Long fileId;

    /**
     * 新文件名称
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
     * 文件后缀（保留字段，目前无用）
     */
    private String fileSuffix;

    /**
     * 文件在存储系统中的唯一定位标识
     *
     * <ul>
     *   <li><b>本地存储</b>：文件系统中的绝对路径，如：
     *     <pre>/home/zblog/file/upload/2026-01/06/xxx.png</pre>
     *   </li>
     *   <li><b>云存储（bucket:key）</b>：
     *     <pre>zblog-2026-01:upload/2026/01/09/xxx.jpg</pre>
     *   </li>
     *   <li><b>云存储（key）</b>：
     *     <pre>upload/2026/01/09/xxx.jpg</pre>
     *   </li>
     * </ul>
     */
    private String storageLocation;

    /**
     * 存储相对路径
     */
    private String fileRelativePath;

    /**
     * 文件访问地址
     */
    private String fileAccessUrl;

    /**
     * 上传方式
     */
    private Integer uploadType;
}
