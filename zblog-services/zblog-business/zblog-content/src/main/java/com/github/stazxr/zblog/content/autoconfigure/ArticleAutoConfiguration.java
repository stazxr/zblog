package com.github.stazxr.zblog.content.autoconfigure;

import com.github.stazxr.zblog.content.autoconfigure.properties.ArticleUploadProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 文章自动装配
 *
 * @author SunTao
 * @since 2026-11-06
 */
@Configuration
@EnableConfigurationProperties(ArticleUploadProperties.class)
public class ArticleAutoConfiguration {
}
