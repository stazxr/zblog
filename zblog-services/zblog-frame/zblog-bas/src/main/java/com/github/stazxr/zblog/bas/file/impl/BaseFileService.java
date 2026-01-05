package com.github.stazxr.zblog.bas.file.impl;

import com.github.stazxr.zblog.bas.encryption.util.Md5Utils;
import com.github.stazxr.zblog.bas.file.FileHandler;
import com.github.stazxr.zblog.bas.file.model.FileInfo;
import com.github.stazxr.zblog.util.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

/**
 * 文件相关接口的默认实现，不同渠道的文件处理实现只需要继承该类即可
 *
 * @author SunTao
 * @since 2022-07-27
 */
public abstract class BaseFileService implements FileHandler {
    /**
     * 获取文件信息
     *
     * @param file MultipartFile
     * @return FileInfo
     * @throws Exception IO ERROR
     */
    protected FileInfo parseMultiFile(MultipartFile file) throws Exception {
        // 获取源文件信息
        String originalFilename = FileUtils.verifyFilename(file.getOriginalFilename());
        String fileSuffix = FileUtils.getExtensionName(originalFilename);

        // 设置文件信息
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFilename(originalFilename);
        fileInfo.setOriginalFilename(originalFilename);
        fileInfo.setFileSize(file.getSize());
        fileInfo.setFileMd5(Md5Utils.getMessageDigest(file.getBytes()));
        fileInfo.setFileType(file.getContentType());
        fileInfo.setFileSuffix(fileSuffix.toLowerCase(Locale.ROOT));
        fileInfo.setInputStream(file.getInputStream());
        return fileInfo;
    }
}
