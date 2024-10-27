package com.github.stazxr.zblog.base.domain.bo.storage;

import com.alibaba.fastjson.JSON;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * 云存储配置信息-阿里云
 *
 * @author SunTao
 * @since 2021-01-25
 */
@Getter
@Setter
public class AliYunConfig extends BaseStorageConfig {
    /**
     * 绑定的域名
     */
    private String domain = "";

    /**
     * 上传路径
     */
    private String pathPrefix = "";

    /**
     * 不同区域对应的访问端点
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
     * Bucket
     */
    private String bucketName = "";

    @Override
    public String toString() {
        return "AliYunConfig{" +
                "domain='" + domain + '\'' +
                ", pathPrefix='" + pathPrefix + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", accessKeyId='" + accessKeyId + '\'' +
                ", accessKeySecret='" + accessKeySecret + '\'' +
                ", bucketName='" + bucketName + '\'' +
                '}';
    }

    /**
     * 从 JSON 字符串中实例化对象
     *
     * @param jsonStr JSON 字符串
     * @return BaseStorageConfig
     */
    public static AliYunConfig instanceFromJson(String jsonStr) {
        if (StringUtils.isNotBlank(jsonStr)) {
            return JSON.parseObject(jsonStr, AliYunConfig.class);
        }

        return new AliYunConfig();
    }
}
