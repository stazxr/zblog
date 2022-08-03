package com.github.stazxr.zblog.core.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ZblogProperties，系统基础配置信息
 *
 * @author SunTao
 * @since 2022-06-07
 */
@Data
@ConfigurationProperties(prefix = "zblog")
public class ZblogProperties {
    /**
     * basePackage
     */
    private String basePackage = "com.github.stazxr.zblog";

    /**
     * 部署类型：single - 单机部署；multi - 多节点部署. 默认单机部署
     */
    private String deployType = "single";

    /**
     * IP 是否采用本地解析. 默认是
     */
    private Boolean isLocalIpParsing = true;

    /**
     * 文件的访问地址
     */
    private String fileDomain;

    /**
     * 文件上传目录
     */
    private String fileUploadPath;

    /**
     * 文件访问域名
     */
    private String fileBaseUrl = "http://localhost:8080/file";

    /**
     * 文件上传类型（1，本地；2，阿里云存储；3，七牛云存储；4，ftp-未实现；5...）
     */
    private int fileUploadType = 1;
}
