package com.github.stazxr.zblog.base.third.file.impl;

import com.github.stazxr.zblog.base.third.file.model.FileInfo;
import com.github.stazxr.zblog.base.third.file.util.LocalStorageUtils;
import com.github.stazxr.zblog.core.config.properties.ZblogProperties;
import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.util.io.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 本地文件存储
 *
 * @author SunTao
 * @since 2022-07-27
 */
@Component("LocalFileHandlerService")
@RequiredArgsConstructor
public class LocalFileHandlerService extends DefaultFileService {
    private final ZblogProperties zblogProperties;

    protected String baseUrl;

    protected String fileUploadPath;

    @PostConstruct
    void initVariable() {
        String fileSpec = "/";
        baseUrl = zblogProperties.getFileBaseUrl();
        if (!baseUrl.endsWith(fileSpec)) {
            baseUrl = baseUrl.concat(fileSpec);
        }

        fileUploadPath = zblogProperties.getFileUploadPath();
        if (!fileUploadPath.endsWith(fileSpec)) {
            fileUploadPath = fileUploadPath.concat(fileSpec);
        }
    }

    /**
     * 文件上传
     *
     * @param files 文件列表
     * @return 上传的文件列表
     */
    @Override
    public List<FileInfo> uploadFile(MultipartFile[] files) {
        try {
            List<FileInfo> fileInfos = new ArrayList<>();
            for (MultipartFile file : files) {
                FileInfo fileInfo = parseMultiFile(file);

                // eg: path/filename
                String relativePath = LocalStorageUtils.uploadLocal(file, fileUploadPath);
                fileInfo.setFileName(FileUtils.getFileName(relativePath));
                fileInfo.setFilePath(fileUploadPath.concat(relativePath));
                fileInfo.setDownloadUrl(baseUrl.concat(relativePath));
                fileInfos.add(fileInfo);
            }
            return fileInfos;
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FILE_UPLOAD_FAILED, e);
        }
    }
}
