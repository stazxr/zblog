package com.github.stazxr.zblog.bas.file.handler.impl;

import com.github.stazxr.zblog.bas.file.autoconfigure.FileProperties;
import com.github.stazxr.zblog.bas.file.handler.AbstractFileHandler;
import com.github.stazxr.zblog.bas.file.handler.FileHandlerEnum;
import com.github.stazxr.zblog.bas.file.model.FileInfo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 本地文件存储
 *
 * @author SunTao
 * @since 2022-07-27
 */
public class LocalFileHandler extends AbstractFileHandler {
    public LocalFileHandler(FileProperties.LocalConfig config) {
        super(config.getFileAccessUrl(), config.getStoragePathPrefix());
    }

    /**
     * 下载文件。
     *
     * @param fileInfo 文件元数据
     * @param inputStream 文件输入流，模板方法已关闭流，实现不应该做关闭操作
     * @throws IOException IO 异常
     */
    @Override
    protected void uploadImpl(FileInfo fileInfo, InputStream inputStream) throws IOException {
        // 获取文件信息，并创建目录
        String storageLocation = fileInfo.getStorageLocation();
        File dest = new File(storageLocation).getCanonicalFile();
        if (!dest.getParentFile().exists() && !dest.getParentFile().mkdirs()) {
            throw new IOException("上传目录创建失败，" + dest.getParentFile().getAbsolutePath());
        }

        // 上传文件
        try (OutputStream out = Files.newOutputStream(dest.toPath())) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        }
    }

    /**
     * 删除文件。
     *
     * @param storageLocation 文件存储系统中的唯一标识路径
     * @throws IOException IO 异常
     */
    @Override
    protected void deleteImpl(String storageLocation) throws IOException {
        Files.deleteIfExists(Paths.get(storageLocation));
    }

    /**
     * 获取文件输入流。
     *
     * @param storageLocation 文件在存储系统中的唯一定位标识
     * @return 文件输入流
     * @throws IOException IO 异常
     */
    @Override
    protected InputStream getFileStream(String storageLocation) throws IOException {
        Path path = Paths.get(storageLocation);
        if (!Files.exists(path)) {
            throw new FileNotFoundException(storageLocation);
        }
        return Files.newInputStream(path);
    }

    /**
     * 获取文件上传类型。
     *
     * @return 上传类型
     */
    @Override
    public FileHandlerEnum getFileUploadType() {
        return FileHandlerEnum.LOCAL;
    }
}
