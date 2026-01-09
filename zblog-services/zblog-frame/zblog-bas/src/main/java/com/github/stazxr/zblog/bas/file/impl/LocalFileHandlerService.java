package com.github.stazxr.zblog.bas.file.impl;

import com.github.stazxr.zblog.bas.exception.ExpMessageCode;
import com.github.stazxr.zblog.bas.file.FileException;
import com.github.stazxr.zblog.bas.file.FileHandlerEnum;
import com.github.stazxr.zblog.bas.file.autoconfigure.FileProperties;
import com.github.stazxr.zblog.bas.file.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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

    public LocalFileHandlerService(FileProperties.LocalConfig config) {
        String baseUrl = config.getBaseUrl();
        if (!baseUrl.endsWith(PATH_SEPARATOR)) {
            baseUrl = baseUrl.concat(PATH_SEPARATOR);
        }
        String fileUploadPath = config.getFileUploadPath();
        if (!fileUploadPath.endsWith(PATH_SEPARATOR)) {
            fileUploadPath = fileUploadPath.concat(PATH_SEPARATOR);
        }

        this.baseUrl = baseUrl;
        this.fileUploadPath = fileUploadPath;
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
            fileInfo.setFileAbsolutePath(fileUploadPath + fileInfo.getFileRelativePath());
            fileInfo.setDownloadUrl(baseUrl + fileInfo.getFileRelativePath());
            uploadFile(fileInfo, file.getInputStream());
            return fileInfo;
        } catch (Exception e) {
            throw new FileException(ExpMessageCode.of("valid.file.bas.uploadFailed"), e);
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
        try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(Paths.get(filepath)));
             ServletOutputStream os = response.getOutputStream()) {
            int len;
            byte[] buffer = new byte[BUFFER_SIZE];
            while((len = bis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
        } catch (Exception e) {
            throw new FileException(ExpMessageCode.of("valid.file.bas.downloadFailed"), e);
        }
    }

    /**
     * 删除文件业务实现
     *
     * @param filepath 文件路径
     */
    @Override
    protected void deleteFileImpl(String filepath) {
        File file = new File(filepath);
        if (file.exists() && file.isFile()) {
            if (!file.delete()) {
                throw new FileException("本地文件删除失败");
            }
        }
    }

    /**
     * 获取文件上传类型
     *
     * @return 上传类型
     */
    @Override
    public int getFileUploadType() {
        return FileHandlerEnum.LOCAL.getType();
    }

    private static void uploadFile(FileInfo fileInfo, InputStream inputStream) throws IOException {
        // 获取文件上传路径
        String filepath = fileInfo.getFileAbsolutePath();

        // 创建目录
        File dest = new File(filepath).getCanonicalFile();
        if (!dest.getParentFile().exists() && !dest.getParentFile().mkdirs()) {
            throw new IOException("上传目录创建失败，" + dest.getParentFile().getAbsolutePath());
        }

        // 上传文件
        try (InputStream is = inputStream;OutputStream out = Files.newOutputStream(dest.toPath())) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = is.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        }
    }
}
