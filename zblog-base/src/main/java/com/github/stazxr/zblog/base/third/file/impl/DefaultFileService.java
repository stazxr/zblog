package com.github.stazxr.zblog.base.third.file.impl;

import com.github.stazxr.zblog.base.third.file.FileHandler;
import com.github.stazxr.zblog.base.third.file.model.FileInfo;
import com.github.stazxr.zblog.util.io.FileUtils;
import com.github.stazxr.zblog.util.secret.Md5Utils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件相关接口的默认实现，不同渠道的文件处理实现只需要继承该类即可
 *
 * @author SunTao
 * @since 2022-07-27
 */
public abstract class DefaultFileService implements FileHandler {
    /**
     * 获取文件信息
     *
     * @param file MultipartFile
     * @return FileInfo
     * @throws Exception IO ERROR
     */
    protected FileInfo parseMultiFile(MultipartFile file) throws Exception {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setOriginalFileName(file.getOriginalFilename());
        fileInfo.setSuffix(FileUtils.getExtensionName(file.getOriginalFilename()));
        fileInfo.setSize(file.getSize());
        fileInfo.setMd5(Md5Utils.getMessageDigest(file.getBytes()));
        return fileInfo;
    }
}
