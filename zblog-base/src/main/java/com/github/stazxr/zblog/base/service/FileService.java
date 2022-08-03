package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.base.domain.entity.File;
import com.github.stazxr.zblog.base.component.file.model.FileInfo;

import java.util.List;

/**
 * 附件上传业务
 *
 * @author SunTao
 * @since 2022-07-27
 */
public interface FileService extends IService<File> {
    /**
     * 批量插入文件
     *
     * @param fileList 文件列表
     * @param fileUploadType 上传方式
     * @return 上传文件列表
     */
    List<File> insertFile(List<FileInfo> fileList, int fileUploadType);
}
