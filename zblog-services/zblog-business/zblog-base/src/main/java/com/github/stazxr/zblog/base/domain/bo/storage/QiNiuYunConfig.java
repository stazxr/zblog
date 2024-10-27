package com.github.stazxr.zblog.base.domain.bo.storage;

import com.alibaba.fastjson.JSON;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * 云存储配置信息-七牛云
 *
 * @author SunTao
 * @since 2021-01-25
 */
@Getter
@Setter
public class QiNiuYunConfig extends BaseStorageConfig {
    /**
     * 绑定的域名
     */
    private String domain = "";

    /**
     * 路径前缀
     */
    private String pathPrefix = "";

    /**
     * AK
     */
    private String ak = "";

    /**
     * SK
     */
    private String sk = "";

    /**
     * 存储区域
     */
    private String zone = "";

    /**
     * 存储空间
     */
    private String bucketName = "";

    @Override
    public String toString() {
        return "QiNiuYunConfig{" +
                "domain='" + domain + '\'' +
                ", pathPrefix='" + pathPrefix + '\'' +
                ", ak='" + ak + '\'' +
                ", sk='" + sk + '\'' +
                ", bucketName='" + bucketName + '\'' +
                '}';
    }

    /**
     * 从 JSON 字符串中实例化对象
     *
     * @param jsonStr JSON 字符串
     * @return BaseStorageConfig
     */
    public static QiNiuYunConfig instanceFromJson(String jsonStr) {
        if (StringUtils.isNotBlank(jsonStr)) {
            return JSON.parseObject(jsonStr, QiNiuYunConfig.class);
        }

        return new QiNiuYunConfig();
    }
}
