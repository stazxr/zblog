package com.github.stazxr.zblog.base.component.file.impl;

import com.github.stazxr.zblog.base.component.file.FileHandler;
import com.github.stazxr.zblog.base.component.file.model.FileInfo;
import com.github.stazxr.zblog.util.io.FileUtils;
import com.github.stazxr.zblog.util.secret.Md5Utils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件相关接口的默认实现，不同渠道的文件处理实现只需要继承该类即可
 *
 * @author SunTao
 * @since 2022-07-27
 */
public abstract class BaseFileService implements FileHandler {
    /**
     * 桶名称与上传路径的分隔符
     */
    protected static final String BUCKET_PATH_SPLIT_LABEL = ":";

    /**
     * 获取文件信息
     *
     * @param file MultipartFile
     * @return FileInfo
     * @throws Exception IO ERROR
     */
    protected FileInfo parseMultiFile(MultipartFile file) throws Exception {
        FileInfo fileInfo = new FileInfo();
        String extensionName = FileUtils.getExtensionName(file.getOriginalFilename());
        String filename = FileUtils.getFileNameNoEx(FileUtils.verifyFilename(file.getOriginalFilename()));
        fileInfo.setOriginalFileName(filename.concat(".").concat(extensionName));
        fileInfo.setSuffix(extensionName);
        fileInfo.setSize(file.getSize());
        fileInfo.setMd5(Md5Utils.getMessageDigest(file.getBytes()));
        return fileInfo;
    }
}
