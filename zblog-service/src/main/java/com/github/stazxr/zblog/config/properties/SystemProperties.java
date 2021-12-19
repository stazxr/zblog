package com.github.stazxr.zblog.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 系统配置信息
 *
 * @author SunTao
 * @since 2021-12-11
 */
@Data
@ConfigurationProperties(prefix = "system")
public class SystemProperties {
    /**
     * 部署类型：single - 单机部署；multi - 多节点部署
     */
    private String deployType;
}
