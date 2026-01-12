package com.github.stazxr.zblog.bas.file.handler.impl;

import com.github.stazxr.zblog.bas.file.handler.AbstractFileHandler;
import com.github.stazxr.zblog.bas.file.model.FileInfo;
import com.github.stazxr.zblog.bas.file.model.StorageLocation;
import com.github.stazxr.zblog.bas.file.util.StorageLocationParser;
import com.github.stazxr.zblog.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 云存储文件处理抽象基类。
 *
 * @author SunTao
 * @since 2026-01-12
 */
public abstract class CloudFileHandler extends AbstractFileHandler {
    /**
     * 默认桶名称（存储空间名称）
     */
    private final String defaultBucket;

    /**
     * 桶与 key 分隔符
     */
    protected static final String BUCKET_KEY_SEPARATOR = ":";

    /**
     * 构造方法。
     *
     * @param fileAccessUrl     文件访问 URL 前缀
     * @param storagePathPrefix 文件存储路径前缀
     * @param defaultBucket     默认桶名称
     */
    public CloudFileHandler(String fileAccessUrl, String storagePathPrefix, String defaultBucket) {
        super(fileAccessUrl, storagePathPrefix);
        if (StringUtils.isBlank(defaultBucket)) {
            throw new IllegalArgumentException("defaultBucket must not be blank");
        }
        this.defaultBucket = defaultBucket;
    }

    /**
     * 文件上传。
     *
     * @param fileInfo    文件元数据
     * @param inputStream 文件输入流，模板方法已关闭流，实现不应该做关闭操作
     * @throws IOException IO 异常
     */
    @Override
    protected void uploadImpl(FileInfo fileInfo, InputStream inputStream) throws IOException {
        String objectKey = buildObjectKey(fileInfo);
        uploadCloudFile(fileInfo, objectKey, inputStream);
    }

    /**
     * 子类实现：云存储上传文件。
     *
     * @param fileInfo    文件元数据
     * @param objectKey   对象在云存储中的唯一标识（Object Key）
     *                    <p>
     *                    该值表示对象在 Bucket 中的逻辑路径，不是本地文件路径，
     *  *                 通常由 {@code storagePathPrefix + relativePath} 构成，
     *                    不应包含 bucket 名称。
     *                    </p>
     * @param inputStream 文件输入流，模板方法已关闭流，实现不应该做关闭操作
     * @throws IOException IO 异常
     */
    protected abstract void uploadCloudFile(FileInfo fileInfo, String objectKey, InputStream inputStream) throws IOException;

    /**
     * 删除文件。
     *
     * @param storageLocation 文件存储系统中的唯一标识路径
     * @throws IOException IO 异常
     */
    @Override
    protected void deleteImpl(String storageLocation) throws IOException {
        deleteCloudFile(StorageLocationParser.parse(storageLocation));
    }

    /**
     * 子类实现：云存储删除文件。
     *
     * @param storageLocation 解析后的存储信息
     * @throws IOException IO 异常
     */
    protected abstract void deleteCloudFile(StorageLocation storageLocation) throws IOException;

    /**
     * 获取文件输入流。
     *
     * @param storageLocation 文件在存储系统中的唯一定位标识
     * @return 文件输入流
     * @throws IOException IO 异常
     */
    @Override
    protected InputStream getFileStream(String storageLocation) throws IOException {
        return getCloudFileStream(StorageLocationParser.parse(storageLocation));
    }

    /**
     * 子类实现：云存储获取文件输入流（下载）。
     *
     * @param storageLocation 解析后的存储信息
     * @return 文件输入流
     * @throws IOException IO 异常
     */
    protected abstract InputStream getCloudFileStream(StorageLocation storageLocation) throws IOException;

    /**
     * 获取实际 bucket。
     *
     * <p>如果 storageLocation 没有指定 bucket，则使用默认 bucket。</p>
     *
     * @param storageLocation 解析后的存储信息
     * @return bucket 名称
     */
    protected String resolveBucket(StorageLocation storageLocation) {
        return storageLocation.hasBucket() ? storageLocation.getBucket() : defaultBucket;
    }

    /**
     * 构建文件在存储系统中的定位标识。
     *
     * @param relativePath 文件相对路径
     * @return 文件在存储系统中的定位标识
     */
    @Override
    protected String buildStorageLocation(String relativePath) {
        String storageLocation = super.buildStorageLocation(relativePath);
        return getDefaultBucket().concat(BUCKET_KEY_SEPARATOR).concat(storageLocation);
    }

    /**
     * 构建文件对外访问 URL，该 URL 通常用于前端访问或下载文件。
     *
     * @param relativePath 文件相对路径
     * @return 文件访问 URL
     */
    @Override
    protected String buildFileAccessUrl(String relativePath) {
        return getFileAccessUrl() + getStoragePathPrefix() + relativePath;
    }

    protected String buildObjectKey(FileInfo fileInfo) {
        return getStoragePathPrefix() + fileInfo.getFileRelativePath();
    }

    /**
     * 获取默认桶名称
     *
     * @return 默认桶
     */
    protected String getDefaultBucket() {
        return defaultBucket;
    }
}
