package com.github.stazxr.zblog.bas.file.autoconfigure.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文件存储配置项
 *
 * @author SunTao
 * @since 2024-10-24
 */
@ConfigurationProperties(prefix= FileProperties.CONFIG_PREFIX)
public class FileProperties {
    static final String CONFIG_PREFIX= "zblog.base.file";

    /**
     * 文件上传模式：see {@link com.github.stazxr.zblog.bas.file.handler.FileHandlerEnum}
     */
    private int model = 1;

    /**
     * 本地配置信息
     */
    private LocalConfig local = new LocalConfig();

    /**
     * 阿里云 oss 配置信息
     */
    private AliyunOssConfig oss = new AliyunOssConfig();

    /**
     * 七牛云 kodo 配置信息
     */
    private QiniuyunKodoConfig kodo = new QiniuyunKodoConfig();

    /**
     * 腾讯云 cos 配置信息
     */
    private TencentyunCosConfig cos = new TencentyunCosConfig();

    @Getter
    @Setter
    public static class LocalConfig {
        /**
         * 是否启用本地文件存储
         */
        private boolean enabled = true;

        /**
         * 文件访问 URL 前缀
         * <p>用于拼接文件对外访问地址</p>
         */
        private String fileAccessUrl = "http://127.0.0.1:8000/file";

        /**
         * 文件存储路径前缀（逻辑路径）
         * <p>用于生成文件在存储系统中的相对路径</p>
         */
        private String storagePathPrefix = "/zblog/upload";
    }

    @Getter
    @Setter
    public static class AliyunOssConfig {
        /**
         * 是否启用阿里云 OSS 文件存储
         */
        private boolean enabled = false;

        /**
         * 文件访问 URL 前缀
         * <p>用于拼接文件对外访问地址</p>
         */
        private String fileAccessUrl;

        /**
         * 文件存储路径前缀（逻辑路径）
         * <p>用于生成文件在存储系统中的相对路径</p>
         */
        private String storagePathPrefix = "upload";

        /**
         * 阿里云 AccessKey ID
         */
        private String accessKey;

        /**
         * 阿里云 AccessKey Secret
         */
        private String secretKey;

        /**
         * 阿里云 Endpoint
         */
        private String endpoint;

        /**
         * 桶名称
         */
        private String bucketName;
    }

    @Getter
    @Setter
    public static class QiniuyunKodoConfig {
        /**
         * 是否启用七牛云对象存储
         */
        private boolean enabled = false;

        /**
         * 文件访问 URL 前缀，七牛云新建空间会附送一个30天的免费域名
         * <p>
         * 来源：登录七牛云 → 控制台 -> 对象存储Kodo → 空间管理（选择一个空间点进去） → 域名管理
         */
        private String fileAccessUrl;

        /**
         * 文件存储路径前缀（逻辑路径）
         * <p>用于生成文件在存储系统中的相对路径</p>
         */
        private String storagePathPrefix = "upload";

        /**
         * 七牛云 AccessKey（AK），来源：登录七牛云 → 个人中心 → 密钥管理 -> 新建秘钥 → AK
         */
        private String accessKey;

        /**
         * 七牛云 SecretKey（SK），来源：登录七牛云 → 个人中心 → 密钥管理 -> 新建秘钥 → SK
         */
        private String secretKey;

        /**
         * 七牛云存储区域，来源：登录七牛云 → 控制台 -> 对象存储Kodo → 空间管理 → 空间名称
         */
        private String zone;

        /**
         * 七牛云存储空间名称，来源：登录七牛云 → 控制台 -> 对象存储Kodo → 空间管理 → 空间名称（没有的话需要新建空间）
         */
        private String zoneName;
    }

    @Getter
    @Setter
    public static class TencentyunCosConfig {
        /**
         * 是否启用腾讯云对象存储
         */
        private boolean enabled = false;

        /**
         * 文件访问 URL 前缀
         * <p>用于拼接文件对外访问地址</p>
         */
        private String fileAccessUrl;

        /**
         * 文件存储路径前缀（逻辑路径）
         * <p>用于生成文件在存储系统中的相对路径</p>
         */
        private String storagePathPrefix = "upload";

        /**
         * 腾讯云访问密钥 ID
         */
        private String accessKey;

        /**
         * 腾讯云访问密钥 Key
         */
        private String secretKey;

        /**
         * COS 区域
         */
        private String region;

        /**
         * 桶名称
         */
        private String bucketName;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public LocalConfig getLocal() {
        return local;
    }

    public void setLocal(LocalConfig local) {
        this.local = local;
    }

    public AliyunOssConfig getOss() {
        return oss;
    }

    public void setOss(AliyunOssConfig oss) {
        this.oss = oss;
    }

    public QiniuyunKodoConfig getKodo() {
        return kodo;
    }

    public void setKodo(QiniuyunKodoConfig kodo) {
        this.kodo = kodo;
    }

    public TencentyunCosConfig getCos() {
        return cos;
    }

    public void setCos(TencentyunCosConfig cos) {
        this.cos = cos;
    }
}
