package com.github.stazxr.zblog.bas.file.autoconfigure;

import com.github.stazxr.zblog.bas.file.FileHandlerEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文件存储配置项
 *
 * @author SunTao
 * @since 2024-10-24
 */
@ConfigurationProperties(prefix= FileProperties.FILE_PREFIX)
public class FileProperties {
    static final String FILE_PREFIX= "zblog.base.file";

    /**
     * 文件上传模式：see {@link FileHandlerEnum}
     */
    private int model = 1;

    /**
     * 本地配置信息
     */
    private LocalConfig local = new LocalConfig();

    /**
     * 阿里云配置信息
     */
    private AliyunConfig aliyun = new AliyunConfig();

    /**
     * 七牛云配置信息
     */
    private QiniuYunConfig qiniuyun = new QiniuYunConfig();

    /**
     * 腾讯云配置信息
     */
    private TencentConfig tencentyun = new TencentConfig();

    @Getter
    @Setter
    public static class LocalConfig {
        /**
         * 是否启用本地文件存储
         */
        private boolean enabled = true;

        /**
         * 本地文件访问地址
         */
        private String baseUrl = "http://127.0.0.1:8000/file";

        /**
         * 本地文件上传根路径
         * <p>
         * 文件在服务器磁盘中的存储根目录，例如：
         * <li>/data/upload/</li>
         * <li>D:/upload/</li>
         */
        private String fileUploadPath = "/zblog/upload";
    }

    @Getter
    @Setter
    public static class AliyunConfig {
        /**
         * 是否启用阿里云 OSS 文件存储
         */
        private boolean enabled = false;

        /**
         * 阿里云 AccessKey ID
         * <p>
         * 用于 OSS API 身份认证
         */
        private String accessKeyId;

        /**
         * 阿里云 AccessKey Secret
         * <p>
         * 与 AccessKey ID 配套使用，请妥善保管
         */
        private String accessKeySecret;

        /**
         * OSS 服务访问 Endpoint
         */
        private String endpoint;

        /**
         * 桶名称
         */
        private String bucketName;

        /**
         * 文件上传路径
         * <p>
         * OSS 中的“目录前缀”，并非真实物理目录，不以斜杠开头！
         */
        private String fileUploadPath = "upload";

        /**
         * 文件访问基础地址
         * <p>
         * 一般为 OSS 外网访问域名或 CDN 域名
         */
        private String baseUrl;
    }

    @Getter
    @Setter
    public static class QiniuYunConfig {
        /**
         * 是否启用七牛云对象存储
         */
        private boolean enabled = false;

        /**
         * 七牛云 AccessKey（AK），来源：登录七牛云 → 个人中心 → 密钥管理 -> 新建秘钥 → AK
         */
        private String ak;

        /**
         * 七牛云 SecretKey（SK），来源：登录七牛云 → 个人中心 → 密钥管理 -> 新建秘钥 → SK
         */
        private String sk;

        /**
         * 七牛云存储区域，来源：登录七牛云 → 控制台 -> 对象存储Kodo → 空间管理 → 空间名称
         */
        private String zone;

        /**
         * 七牛云存储空间名称，来源：登录七牛云 → 控制台 -> 对象存储Kodo → 空间管理 → 空间名称（没有的话需要新建空间）
         */
        private String zoneName;

        /**
         * 文件存储路径前缀（相对于存储空间的根路径），不以斜杠开头！
         */
        private String fileUploadPath = "upload";

        /**
         * 七牛云存储空间的访问域名（文件公网访问地址），七牛云新建空间会附送一个30天的免费域名
         * <p>
         * 来源：登录七牛云 → 控制台 -> 对象存储Kodo → 空间管理（选择一个空间点进去） → 域名管理
         */
        private String baseUrl;
    }

    @Getter
    @Setter
    public static class TencentConfig {

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

    public AliyunConfig getAliyun() {
        return aliyun;
    }

    public void setAliyun(AliyunConfig aliyun) {
        this.aliyun = aliyun;
    }

    public QiniuYunConfig getQiniuyun() {
        return qiniuyun;
    }

    public void setQiniuyun(QiniuYunConfig qiniuyun) {
        this.qiniuyun = qiniuyun;
    }

    public TencentConfig getTencentyun() {
        return tencentyun;
    }

    public void setTencentyun(TencentConfig tencentyun) {
        this.tencentyun = tencentyun;
    }
}
