package com.github.stazxr.zblog.base.third.file;

import com.github.stazxr.zblog.base.third.file.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件处理方式定义
 *
 * @author SunTao
 * @since 2022-07-27
 */
public interface FileHandler {
    /**
     * 文件上传
     *
     * @param files 文件列表
     * @return 上传的文件列表
     */
    List<FileInfo> uploadFile(MultipartFile[] files);
}
