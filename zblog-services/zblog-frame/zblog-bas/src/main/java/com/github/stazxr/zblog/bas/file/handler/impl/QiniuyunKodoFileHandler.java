package com.github.stazxr.zblog.bas.file.handler.impl;

import com.github.stazxr.zblog.bas.file.autoconfigure.FileProperties;
import com.github.stazxr.zblog.bas.file.handler.FileHandlerEnum;
import com.github.stazxr.zblog.bas.file.model.FileInfo;
import com.github.stazxr.zblog.bas.file.model.StorageLocation;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 七牛云对象存储（Kodo）文件处理实现。
 *
 * <p>
 * 基于 {@link CloudFileHandler}，封装七牛云 Kodo SDK 的上传、删除与下载逻辑。
 * </p>
 *
 * @author SunTao
 * @since 2026-01-12
 */
public class QiniuyunKodoFileHandler extends CloudFileHandler {
    /**
     * 七牛云鉴权对象
     */
    private final Auth auth;
    /**
     * 上传管理器
     */
    private final UploadManager uploadManager;
    /**
     * 桶管理器
     */
    private final BucketManager bucketManager;

    public QiniuyunKodoFileHandler(FileProperties.QiniuyunKodoConfig config) {
        super(config.getFileAccessUrl(), config.getStoragePathPrefix(), config.getZoneName());

        //
        this.auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        //
        Configuration cfg = new Configuration(parseRegion(config.getZone()));
        this.uploadManager = new UploadManager(cfg);
        //
        this.bucketManager = new BucketManager(auth, cfg);
    }

    /**
     * 云存储上传文件。
     *
     * @param fileInfo    文件元数据
     * @param objectKey   对象在云存储中的唯一标识（Object Key）
     * @param inputStream 文件输入流，模板方法已关闭流，实现不应该做关闭操作
     * @throws IOException IO 异常
     */
    @Override
    protected void uploadCloudFile(FileInfo fileInfo, String objectKey, InputStream inputStream) throws IOException {
        String uploadToken = auth.uploadToken(getDefaultBucket());
        uploadManager.put(inputStream, objectKey, uploadToken, null, null);
    }

    /**
     * 云存储删除文件。
     *
     * @param storageLocation 解析后的存储信息
     * @throws IOException IO 异常
     */
    @Override
    protected void deleteCloudFile(StorageLocation storageLocation) throws IOException {
        String bucketName = resolveBucket(storageLocation);
        String key = storageLocation.getKey();
        bucketManager.delete(bucketName, key);
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
        // 1. 构建公开访问基础 URL（bucket 域名 + key）
        String key = storageLocation.getKey();
        String baseUrl = getFileAccessUrl() + key;

        // 2. 生成私有下载 URL（默认 1 小时有效）
        long expires = 3600;
        String privateUrl = auth.privateDownloadUrl(baseUrl, expires);

        // 3. 通过 HTTP 拉取文件流
        URL url = new URL(privateUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(15000);
        conn.setRequestMethod("GET");
        int code = conn.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK) {
            throw new IOException("Qiniu download failed, httpCode=" + code);
        }

        return conn.getInputStream();
    }

    /**
     * 获取文件上传类型。
     *
     * @return 上传类型
     */
    @Override
    public FileHandlerEnum getFileUploadType() {
        return FileHandlerEnum.QINIUYUN;
    }

    /**
     * 解析七牛云区域标识。
     */
    private Region parseRegion(String zone) {
        switch (zone) {
            case "huadong":
                return Region.huadong();
            case "huabei":
                return Region.huabei();
            case "huanan":
                return Region.huanan();
            case "beimei":
                return Region.beimei();
            case "xinjiapo":
                return Region.xinjiapo();
            default:
                return Region.autoRegion();
        }
    }
}
