package com.github.stazxr.zblog.base.config;

import com.github.stazxr.zblog.base.config.properties.DeployProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * ZblogBase 配置中心
 *
 * @author SunTao
 * @since 2022-01-28
 */
@EnableConfigurationProperties(value = {
        DeployProperties.class
})
public class ZblogBaseConfig {
}
