package com.github.stazxr.zblog.util.ali;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 阿里云存储配置工具类
 *   阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维
 *   请登录 https://ram.console.aliyun.com 创建RAM账号
 *
 * @author SunTao
 * @since 2021-02-19
 */
@Slf4j
public class AliYunUtils {
    /**
     * 获取阿里云对象存储配置信息
     *
     * @param endpoint    EndPoint
     * @param accessKeyId accessKeyId
     * @param secret      accessKeySecret
     * @param bucket      存储空间
     * @return AliYunOssConfig
     */
    public static AliYunOssConfig getOssConfig(String endpoint, String accessKeyId, String secret, String bucket) {
        AliYunOssConfig ossConfig = new AliYunOssConfig();
        ossConfig.setEndpoint(endpoint);
        ossConfig.setAccessKeyId(accessKeyId);
        ossConfig.setAccessKeySecret(secret);
        ossConfig.setBucketName(bucket);
        return ossConfig;
    }

    /**
     * 创建存储空间
     *
     * @param config     配置信息
     * @param bucketName 桶名称
     */
    public static void createBucket(AliYunOssConfig config, String bucketName) {
        // 创建OSSClient实例
        OSS ossClient = createOssInstance(config);

        try {
            // 创建存储空间
            if (ossClient.doesBucketExist(bucketName)) {
                throw new IllegalArgumentException("Bucket【" + bucketName + "】已存在");
            }

            ossClient.createBucket(bucketName);
        } finally {
            shutdownOss(ossClient);
        }
    }

    /**
     * 文件上传
     *
     * @param config 配置信息
     * @param bucketName 桶名称
     * @param filePath 文件路径
     * @param uploadBytes 上传内容
     */
    public static void uploadFile(AliYunOssConfig config, String bucketName, String filePath, byte[] uploadBytes) {
        // 创建OSSClient实例
        OSS ossClient = createOssInstance(config);

        try {
            // 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称（filePath）
            if (!ossClient.doesBucketExist(bucketName)) {
                throw new IllegalArgumentException("Bucket【" + bucketName + "】不存在，请检查名称");
            }

            ossClient.putObject(bucketName, filePath, new ByteArrayInputStream(uploadBytes));
        } finally {
            shutdownOss(ossClient);
        }
    }

    /**
     * 文件下载
     *
     * @param config 配置信息
     * @param bucketName 桶名称
     * @param filePath 文件路径
     * @param response 响应对象
     * @throws IOException 异常信息
     */
    public static void downloadFile(AliYunOssConfig config, String bucketName, String filePath, HttpServletResponse response) throws IOException {
        // 创建OSSClient实例
        OSS ossClient = createOssInstance(config);

        try {
            // 调用ossClient.getObject返回一个OSSObject实例，该实例包含文件内容及文件元信息。
            OSSObject ossObject = ossClient.getObject(bucketName, filePath);

            // 调用ossObject.getObjectContent获取文件输入流，可读取此输入流获取其内容
            try (InputStream is = ossObject.getObjectContent();ServletOutputStream os = response.getOutputStream()) {
                int len;
                byte[] buffer = new byte[1024];
                while((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
            }
        } finally {
            shutdownOss(ossClient);
        }
    }

    /**
     * 文件删除
     *
     * @param config 配置信息
     * @param bucketName 桶名称
     * @param filePath 文件路径
     */
    public static void deleteFile(AliYunOssConfig config, String bucketName, String filePath) {
        // 创建OSSClient实例
        OSS ossClient = createOssInstance(config);

        try {
            // 如果文件存在则删除
            if (ossClient.doesObjectExist(bucketName, filePath)) {
                ossClient.deleteObject(bucketName, filePath);
            }
        } finally {
            shutdownOss(ossClient);
        }
    }

    private static OSS createOssInstance(AliYunOssConfig config) {
        String endpoint = config.getEndpoint();
        String accessKeyId = config.getAccessKeyId();
        String accessKeySecret = config.getAccessKeySecret();
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    private static void shutdownOss(OSS ossClient) {
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }
}
