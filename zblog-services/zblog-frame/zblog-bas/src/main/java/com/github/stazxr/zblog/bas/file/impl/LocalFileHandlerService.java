package com.github.stazxr.zblog.bas.file.impl;

import com.github.stazxr.zblog.bas.file.FileException;
import com.github.stazxr.zblog.bas.file.FileTypeHandler;
import com.github.stazxr.zblog.bas.file.model.FileInfo;
import com.github.stazxr.zblog.bas.file.util.LocalStorageUtils;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 本地文件存储
 *
 * @author SunTao
 * @since 2022-07-27
 */
public class LocalFileHandlerService extends BaseFileService {
    private String baseUrl;

    private String fileUploadPath;

    public LocalFileHandlerService(String baseUrl, String fileUploadPath) {
        String fileSpec = "/";
        if (!baseUrl.endsWith(fileSpec)) {
            this.baseUrl = baseUrl.concat(fileSpec);
        }
        if (!fileUploadPath.endsWith(fileSpec)) {
            this.fileUploadPath = fileUploadPath.concat(fileSpec);
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
                fileInfo.setUploadType(FileTypeHandler.LOCAL.getType());
                fileInfos.add(fileInfo);
            }
            return fileInfos;
        } catch (Exception e) {
            throw new FileException("[Local] File upload field", e);
        }
    }

    /**
     * 删除文件
     *
     * @param filepath 文件路径
     */
    @Override
    public void deleteFile(String filepath) {
        if (StringUtils.isNotBlank(filepath)) {
            File file = new File(filepath);
            if (file.exists() && file.isFile()) {
                if (!file.delete()) {
                    throw new FileException("[Local] File delete field");
                }
            }
        }
    }

    /**
     * 批量删除文件
     *
     * @param filepathList 文件路径列表
     */
    @Override
    public void deleteFiles(List<String> filepathList) {
        if (filepathList != null && filepathList.size() > 0) {
            for (String filepath : filepathList) {
                deleteFile(filepath);
            }
        }
    }

    /**
     * 下载文件
     *
     * @param filepath 文件路径
     * @param response 响应对象
     */
    @Override
    public void downloadFile(String filepath, HttpServletResponse response) {
        Assert.notNull(filepath, "待下载文件路径不能为空");
        try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(Paths.get(filepath)));
                ServletOutputStream os = response.getOutputStream()) {
            int len;
            byte[] buffer = new byte[1024];
            while((len = bis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
        } catch (Exception e) {
            throw new FileException("[Local] File download field", e);
        }
    }
}
