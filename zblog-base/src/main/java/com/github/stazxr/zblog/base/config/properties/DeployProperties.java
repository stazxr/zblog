package com.github.stazxr.zblog.base.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * DeployProperties
 *
 * @author SunTao
 * @since 2022-01-28
 */
@Data
@ConfigurationProperties(prefix = "deploy")
public class DeployProperties {
    /**
     * 部署类型：single - 单机部署；multi - 多节点部署.
     */
    private String type = "single";
}
