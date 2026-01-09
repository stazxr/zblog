package com.github.stazxr.zblog.bas.file.impl;

import com.github.stazxr.zblog.bas.encryption.util.Md5Utils;
import com.github.stazxr.zblog.bas.exception.ExpMessageCode;
import com.github.stazxr.zblog.bas.file.FileException;
import com.github.stazxr.zblog.bas.file.FileHandler;
import com.github.stazxr.zblog.bas.file.model.FileInfo;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.io.FileUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
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
     * 缓存大小
     */
    protected static final int BUFFER_SIZE = 8192;

    /**
     * 桶名称与上传路径的分隔符
     */
    protected static final String BUCKET_PATH_SPLIT_LABEL = ":";

    /**
     * 目录分隔符
     */
    protected static final String PATH_SEPARATOR = "/";

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

        // 生成文件名
        Long fileId = SequenceUtils.getId();
        String filename = String.valueOf(fileId);
        if (StringUtils.isNotBlank(fileSuffix)) {
            filename = filename + "." + fileSuffix;
        }

        // 文件相对路径
        String relativePath = buildRelativePath(filename);

        // 设置文件信息
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileId(fileId);
        fileInfo.setFilename(filename);
        fileInfo.setOriginalFilename(originalFilename);
        fileInfo.setFileSize(file.getSize());
        fileInfo.setFileMd5(Md5Utils.md5(file.getInputStream()));
        fileInfo.setFileType(file.getContentType());
        fileInfo.setFileRelativePath(relativePath);
        fileInfo.setFileSuffix(fileSuffix.toLowerCase(Locale.ROOT));
        fileInfo.setUploadType(getFileUploadType());
        return fileInfo;
    }

    /**
     * 删除文件
     *
     * @param filepath 文件路径
     */
    @Override
    public void deleteFile(String filepath) {
        if (StringUtils.isBlank(filepath)) {
            return;
        }

        try {
            deleteFileImpl(filepath);
        } catch (Exception e) {
            throw new FileException(ExpMessageCode.of("valid.file.bas.deleteFailed"), e);
        }
    }

    /**
     * 删除文件业务实现
     *
     * @param filepath 文件路径
     */
    protected abstract void deleteFileImpl(String filepath);

    protected String buildRelativePath(String filename) {
        return DateUtils.formatNow("yyyy/MM/dd/") + filename;
    }
}
