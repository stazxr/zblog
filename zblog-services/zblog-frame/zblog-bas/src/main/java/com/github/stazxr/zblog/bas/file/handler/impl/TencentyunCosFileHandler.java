package com.github.stazxr.zblog.bas.file.handler.impl;

import com.github.stazxr.zblog.bas.file.autoconfigure.FileProperties;
import com.github.stazxr.zblog.bas.file.handler.FileHandlerEnum;
import com.github.stazxr.zblog.bas.file.model.FileInfo;
import com.github.stazxr.zblog.bas.file.model.StorageLocation;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;

import javax.annotation.PreDestroy;
import java.io.InputStream;

/**
 * 腾讯云 COS 文件存储实现。
 *
 * <p>
 * 基于 {@link CloudFileHandler}，封装腾讯云 COS SDK，提供文件的上传、删除与下载能力。
 * </p>
 *
 * @author SunTao
 * @since 2026-01-12
 */
public class TencentyunCosFileHandler extends CloudFileHandler {
    /**
     * 腾讯云 COS 客户端
     */
    private final COSClient cosClient;

    /**
     * 构造方法。
     *
     * <p>
     * 初始化 COSClient，完成访问凭证与区域配置。
     * </p>
     *
     * @param config 腾讯云 COS 配置
     */
    public TencentyunCosFileHandler(FileProperties.TencentyunCosConfig config) {
        super(config.getFileAccessUrl(), config.getStoragePathPrefix(), config.getBucketName());
        COSCredentials credentials = new BasicCOSCredentials(config.getAccessKey(), config.getSecretKey());
        ClientConfig clientConfig = new ClientConfig(new Region(config.getRegion()));
        this.cosClient = new COSClient(credentials, clientConfig);
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
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileInfo.getFileSize());
        PutObjectRequest request = new PutObjectRequest(getDefaultBucket(), objectKey, inputStream, metadata);
        cosClient.putObject(request);
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
        cosClient.deleteObject(bucketName, key);
    }

    /**
     * 云存储获取文件输入流（下载）。
     *
     * @param storageLocation 解析后的存储信息
     * @return 文件输入流
     */
    @Override
    protected InputStream getCloudFileStream(StorageLocation storageLocation) {
        String bucketName = resolveBucket(storageLocation);
        String key = storageLocation.getKey();
        COSObject cosObject = cosClient.getObject(bucketName, key);
        return cosObject.getObjectContent();
    }

    /**
     * 获取文件上传类型。
     *
     * @return 上传类型
     */
    @Override
    public FileHandlerEnum getFileUploadType() {
        return FileHandlerEnum.TENCENTYUN;
    }

    /**
     * 释放 COSClient 资源。
     */
    @PreDestroy
    public void shutdown() {
        if (cosClient != null) {
            cosClient.shutdown();
        }
    }
}
