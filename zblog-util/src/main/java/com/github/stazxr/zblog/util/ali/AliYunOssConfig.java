package com.github.stazxr.zblog.util.ali;

import lombok.Getter;
import lombok.Setter;

/**
 * 阿里云对象存储配置信息
 *
 * @author SunTao
 * @since 2021-01-25
 */
@Getter
@Setter
public class AliYunOssConfig {
    /**
     * endpoint
     */
    private String endpoint = "";

    /**
     * accessKeyId
     */
    private String accessKeyId = "";

    /**
     * accessKeySecret
     */
    private String accessKeySecret = "";

    /**
     * 存储空间
     */
    private String bucketName = "";

    @Override
    public String toString() {
        return "AliYunOssConfig{" +
                ", endpoint='" + endpoint + '\'' +
                ", accessKeyId='" + accessKeyId + '\'' +
                ", accessKeySecret='" + accessKeySecret + '\'' +
                ", bucketName='" + bucketName + '\'' +
                '}';
    }
}
