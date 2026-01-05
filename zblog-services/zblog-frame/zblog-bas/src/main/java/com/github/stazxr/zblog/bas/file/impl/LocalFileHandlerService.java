package com.github.stazxr.zblog.bas.file.impl;

import com.github.stazxr.zblog.bas.exception.ExpMessageCode;
import com.github.stazxr.zblog.bas.file.FileException;
import com.github.stazxr.zblog.bas.file.model.FileInfo;
import com.github.stazxr.zblog.bas.file.util.LocalStorageUtils;
import com.github.stazxr.zblog.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 本地文件存储
 *
 * @author SunTao
 * @since 2022-07-27
 */
public class LocalFileHandlerService extends BaseFileService {
    private final String baseUrl;

    private final String fileUploadPath;

    public LocalFileHandlerService(String baseUrl, String fileUploadPath) {
        String fileSpec = "/";
        if (!baseUrl.endsWith(fileSpec)) {
            baseUrl = baseUrl.concat(fileSpec);
        }
        this.baseUrl = baseUrl;
        if (!fileUploadPath.endsWith(fileSpec)) {
            fileUploadPath = fileUploadPath.concat(fileSpec);
        }
        this.fileUploadPath = fileUploadPath;
    }

    /**
     * 下载文件
     *
     * @param filepath 文件路径
     * @param response 响应对象
     */
    @Override
    public void downloadFile(String filepath, HttpServletResponse response) {
        try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(Paths.get(filepath)));
             ServletOutputStream os = response.getOutputStream()) {
            int len;
            byte[] buffer = new byte[1024];
            while((len = bis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
        } catch (Exception e) {
            throw new FileException(ExpMessageCode.of("valid.file.bas.downloadFailed"));
        }
    }

    /**
     * 文件上传
     *
     * @param file 文件列表
     * @return 上传的文件列表
     */
    @Override
    public FileInfo uploadFile(MultipartFile file) {
        try {
            FileInfo fileInfo = parseMultiFile(file);
            LocalStorageUtils.uploadFile(fileInfo, baseUrl, fileUploadPath);
            return fileInfo;
        } catch (Exception e) {
            throw new FileException(ExpMessageCode.of("valid.file.bas.uploadFailed"), e);
        }
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

        File file = new File(filepath);
        if (file.exists() && file.isFile()) {
            if (!file.delete()) {
                throw new FileException(ExpMessageCode.of("valid.file.bas.deleteFailed"));
            }
        }
    }
}
