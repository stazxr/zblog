package com.github.stazxr.zblog.bas.cors;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * CORS 配置属性。
 *
 * @author Sun Tao
 * @since 2026-09-28
 */
@ConfigurationProperties(prefix = CorsProperties.CONFIG_PREFIX)
public class CorsProperties implements Serializable {
    private static final long serialVersionUID = -6791959558295388176L;

    static final String CONFIG_PREFIX = "zblog.base.cors";

    /**
     * 转换后的允许跨域来源数组。
     */
    private String[] allowedOriginPatternArray = new String[0];

    /**
     * 设置跨域来源
     *
     * @param allowedOriginPatterns 允许的跨域来源，多个逗号分割
     */
    public void setAllowedOriginPatterns(String allowedOriginPatterns) {
        if (allowedOriginPatterns == null || allowedOriginPatterns.trim().isEmpty()) {
            this.allowedOriginPatternArray = new String[0];
            return;
        }

        this.allowedOriginPatternArray = java.util.Arrays.stream(allowedOriginPatterns.split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .toArray(String[]::new);
    }

    public String[] getAllowedOriginPatternArray() {
        return allowedOriginPatternArray;
    }
}