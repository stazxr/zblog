package com.github.stazxr.zblog.base.domain.bo.storage;

import com.alibaba.fastjson.JSON;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * 云存储配置信息-本地配置
 *
 * @author SunTao
 * @since 2021-01-25
 */
@Getter
@Setter
public class LocalConfig extends BaseStorageConfig {
    /**
     * 访问路径前缀
     */
    private String domain = "";

    /**
     * 文件上传目录
     */
    private String uploadFolder = "";

    @Override
    public String toString() {
        return "LocalConfig{" +
                "domain='" + domain + '\'' +
                ", uploadFolder='" + uploadFolder + '\'' +
                '}';
    }

    /**
     * 从 JSON 字符串中实例化对象
     *
     * @param jsonStr JSON 字符串
     * @return BaseStorageConfig
     */
    public static LocalConfig instanceFromJson(String jsonStr) {
        if (StringUtils.isNotBlank(jsonStr)) {
            return JSON.parseObject(jsonStr, LocalConfig.class);
        }

        return new LocalConfig();
    }
}
