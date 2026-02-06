package com.github.stazxr.zblog.bas.rest.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 统一响应配置属性
 * 
 * @author SunTao
 * @since 2026-01-25
 */
@ConfigurationProperties(prefix = RestProperties.CONFIG_PREFIX)
public class RestProperties {
    static final String CONFIG_PREFIX = "zblog.base.rest";

    /**
     * 错误路径，默认 /error
     */
    private String errorPath = "/error";

    /**
     * 不进行统一包装的 URI 列表
     */
    private List<String> notPackageUris = new ArrayList<>();

    public String getErrorPath() {
        return errorPath;
    }

    public void setErrorPath(String errorPath) {
        this.errorPath = errorPath;
    }

    public List<String> getNotPackageUris() {
        return notPackageUris;
    }

    public void setNotPackageUris(List<String> notPackageUris) {
        this.notPackageUris = notPackageUris;
    }
}
