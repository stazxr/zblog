package com.github.stazxr.zblog.util.qiniu;

import com.alibaba.fastjson.JSON;
import com.github.stazxr.zblog.util.Assert;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * 七牛云存储工具类
 *
 * @author SunTao
 * @since 2021-02-19
 */
@Slf4j
public class QiNiuYunUtil {
    /**
     * 获取阿里云对象存储配置信息
     *
     * @param ak     AK
     * @param sk     SK
     * @param zone   存储区域
     * @param bucket 存储空间
     * @return QiNiuYunOssConfig
     */
    public static QiNiuYunOssConfig getOssConfig(String ak, String sk, String zone, String bucket) {
        QiNiuYunOssConfig ossConfig = new QiNiuYunOssConfig();
        ossConfig.setAk(ak);
        ossConfig.setSk(sk);
        ossConfig.setZone(zone);
        ossConfig.setBucketName(bucket);
        return ossConfig;
    }

    /**
     * 文件上传
     *
     * @param config      配置信息
     * @param bucketName  存储空间
     * @param filePath    文件路径
     * @param uploadBytes 上传内容
     * @return QiNiuPutRet
     */
    public static QiNiuPutRet uploadFile(QiNiuYunOssConfig config, String bucketName, String filePath, byte[] uploadBytes) {
        try {
            Configuration cfg = new Configuration(getRegionByName(config.getZone()));
            UploadManager uploadManager = new UploadManager(cfg);
            Auth auth = Auth.create(config.getAk(), config.getSk());
            String upToken = auth.uploadToken(bucketName);

            Response ret = uploadManager.put(uploadBytes, filePath, upToken);
            return JSON.parseObject(ret.bodyString(), QiNiuPutRet.class);
        } catch (QiniuException ex) {
            Response r = ex.response;
            try {
                throw new QiNiuBiException("七牛云文件上传失败: " + r.bodyString(), ex);
            } catch (QiniuException ignored) {
                throw new QiNiuBiException("七牛云文件上传失败", ex);
            }
        }
    }

    /**
     * 删除文件
     *
     * @param config     配置信息
     * @param bucketName 存储空间
     * @param filePath   文件路径
     */
    public static void deleteFile(QiNiuYunOssConfig config, String bucketName, String filePath) {
        try {
            Configuration cfg = new Configuration(getRegionByName(config.getZone()));
            Auth auth = Auth.create(config.getAk(), config.getSk());
            BucketManager bucketManager = new BucketManager(auth, cfg);

            if (bucketManager.stat(bucketName, filePath) != null) {
                bucketManager.delete(bucketName, filePath);
            }
        } catch (QiniuException ex) {
            Response r = ex.response;
            try {
                throw new QiNiuBiException("七牛云文件删除失败: " + r.bodyString(), ex);
            } catch (QiniuException ignored) {
                throw new QiNiuBiException("七牛云文件删除失败", ex);
            }
        }
    }

    /**
     * 批量删除文件
     *
     * @param config     配置信息
     * @param bucketName 存储空间
     * @param filePaths  文件路径
     */
    public static void batchDeleteFile(QiNiuYunOssConfig config, String bucketName, String[] filePaths) {
        try {
            Configuration cfg = new Configuration(getRegionByName(config.getZone()));
            Auth auth = Auth.create(config.getAk(), config.getSk());
            BucketManager bucketManager = new BucketManager(auth, cfg);

            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucketName, filePaths);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < filePaths.length; i++) {
                BatchStatus status = batchStatusList[i];
                if (status.code != 200) {
                    log.warn("failed delete file[{}]: {}:{}", status.data.error, bucketName, filePaths[i]);
                }
            }
        } catch (QiniuException ex) {
            Response r = ex.response;
            try {
                throw new QiNiuBiException("七牛云文件删除失败: " + r.bodyString(), ex);
            } catch (QiniuException ignored) {
                throw new QiNiuBiException("七牛云文件删除失败", ex);
            }
        }
    }

    /**
     * 文件下载
     *
     * @param downloadUrl 下载地址
     * @param response    响应对象
     */
    public static void downloadFile(String downloadUrl, HttpServletResponse response) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).build();

        // 这里默认文件存储空间是共有的，私有需要另外获取 privateDownloadUrl
        try (okhttp3.Response resp = client.newCall(request).execute()) {
            if (resp.isSuccessful()) {
                ResponseBody body = resp.body();
                Assert.notNull(body, "七牛云文件下载，请求响应为空");
                try (InputStream is = body.byteStream(); ServletOutputStream os = response.getOutputStream()) {
                    int len;
                    byte[] buffer = new byte[1024];
                    while ((len = is.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                    }
                }
            }
        } catch (IOException ex) {
            throw new QiNiuBiException("七牛云文件下载失败", ex);
        }
    }

    private static Region getRegionByName(String zone) {
        switch (zone) {
            case "华东":
                return Region.huadong();
            case "华北":
                return Region.huabei();
            case "华南":
                return Region.huanan();
            case "北美":
                return Region.beimei();
            default:
                return Region.autoRegion();
        }
    }
}
