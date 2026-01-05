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
}
