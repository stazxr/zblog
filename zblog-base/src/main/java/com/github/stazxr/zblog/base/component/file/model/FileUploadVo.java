package com.github.stazxr.zblog.base.component.file.model;

import lombok.Data;

/**
 * 上传文件的返回列表
 *
 * @author SunTao
 * @since 2022-07-27
 */
@Data
public class FileUploadVo {
    /**
     * 文件编码
     */
    private String fileId;

    /**
     * 下载地址
     */
    private String downUrl;

    /**
     * 文件原名称
     */
    private String fileName;
}
