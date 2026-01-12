package com.github.stazxr.zblog.bas.file.handler.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.github.stazxr.zblog.bas.exception.ExpMessageCode;
import com.github.stazxr.zblog.bas.file.FileException;
import com.github.stazxr.zblog.bas.file.autoconfigure.FileProperties;
import com.github.stazxr.zblog.bas.file.handler.FileHandlerEnum;
import com.github.stazxr.zblog.bas.file.model.FileInfo;
import com.github.stazxr.zblog.bas.file.model.StorageLocation;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.InputStream;

/**
 * 阿里云 OSS 文件存储实现。
 *
 * <p>
 * 基于 {@link CloudFileHandler}，封装阿里云 OSS SDK 的具体调用逻辑，实现文件的上传、删除与下载能力。
 * </p>
 *
 * @author SunTao
 * @since 2026-01-12
 */
public class AliyunOssFileHandler extends CloudFileHandler {
    /**
     * 阿里云 OSS 客户端
     */
    private final OSS ossClient;

    /**
     * 构造方法。
     *
     * <p>
     * 初始化 OSS 客户端，并校验默认 Bucket 是否存在。
     * </p>
     *
     * @param config 阿里云 OSS 配置
     */
    public AliyunOssFileHandler(FileProperties.AliyunOssConfig config) {
        super(config.getFileAccessUrl(), config.getStoragePathPrefix(), config.getBucketName());

        // 创建 OSS 客户端
        ossClient = new OSSClientBuilder().build(config.getEndpoint(), config.getAccessKey(), config.getSecretKey());

        // 检查桶是否存在
        if (!ossClient.doesBucketExist(config.getBucketName())) {
            throw new FileException(ExpMessageCode.of("valid.file.bas.bucketNotExist", config.getBucketName()));
        }
    }

    /**
     * 云存储上传文件。
     *
     * @param fileInfo    文件元数据
     * @param objectKey   对象在云存储中的唯一标识（Object Key）
     * @param inputStream 文件输入流，模板方法已关闭流，实现不应该做关闭操作
     */
    @Override
    protected void uploadCloudFile(FileInfo fileInfo, String objectKey, InputStream inputStream) {
        ossClient.putObject(getDefaultBucket(), objectKey, inputStream);
    }

    /**
     * 云存储删除文件。
     *
     * @param storageLocation 解析后的存储信息
     */
    @Override
    protected void deleteCloudFile(StorageLocation storageLocation) {
        String bucketName = resolveBucket(storageLocation);
        String key = storageLocation.getKey();
        ossClient.deleteObject(bucketName, key);
    }

    /**
     * 云存储获取文件输入流（下载）。
     *
     * @param storageLocation 解析后的存储信息
     * @return 文件输入流
     * @throws IOException IO 异常
     */
    @Override
    protected InputStream getCloudFileStream(StorageLocation storageLocation) throws IOException {
        String bucketName = resolveBucket(storageLocation);
        String key = storageLocation.getKey();

        // 调用ossClient.getObject返回一个OSSObject实例，该实例包含文件内容及文件元信息。
        OSSObject ossObject = ossClient.getObject(bucketName, key);
        return ossObject.getObjectContent();
    }

    /**
     * 获取文件上传类型。
     *
     * @return 上传类型
     */
    @Override
    public FileHandlerEnum getFileUploadType() {
        return FileHandlerEnum.ALIYUN;
    }

    @PreDestroy
    public void shutdown() {
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }
}
