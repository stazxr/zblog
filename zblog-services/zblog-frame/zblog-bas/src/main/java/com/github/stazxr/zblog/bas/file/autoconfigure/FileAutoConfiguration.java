package com.github.stazxr.zblog.bas.file.autoconfigure;

import com.github.stazxr.zblog.bas.file.handler.FileHandler;
import com.github.stazxr.zblog.bas.file.handler.impl.AliyunOssFileHandler;
import com.github.stazxr.zblog.bas.file.handler.impl.LocalFileHandler;
import com.github.stazxr.zblog.bas.file.handler.impl.QiniuyunKodoFileHandler;
import com.github.stazxr.zblog.bas.file.handler.impl.TencentyunCosFileHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件存储自动配置
 *
 * @author SunTao
 * @since 2024-04-07
 */
@Configuration
@EnableConfigurationProperties({FileProperties.class})
public class FileAutoConfiguration {
    /**
     * 本地文件存储
     *
     * @param properties 文件存储配置信息
     * @return LocalFileHandler
     */
    @Bean("LocalFileHandler")
    @ConditionalOnProperty(prefix = "zblog.base.file.local", name = "enabled", havingValue = "true")
    public FileHandler localFileHandler(FileProperties properties) {
        return new LocalFileHandler(properties.getLocal());
    }

    /**
     * 阿里云对象存储 OSS
     *
     * @param properties 文件存储配置信息
     * @return AliyunOssFileHandler
     */
    @Bean("AliyunOssFileHandler")
    @ConditionalOnProperty(prefix = "zblog.base.file.oss", name = "enabled", havingValue = "true")
    public FileHandler aliyunOssFileHandler(FileProperties properties) {
        return new AliyunOssFileHandler(properties.getOss());
    }

    /**
     * 七牛云对象存储 Kodo
     *
     * @param properties 文件存储配置信息
     * @return QiniuyunKodoFileHandler
     */
    @Bean("QiniuyunKodoFileHandler")
    @ConditionalOnProperty(prefix = "zblog.base.file.kodo", name = "enabled", havingValue = "true")
    public FileHandler qiniuyunKodoFileHandler(FileProperties properties) {
        return new QiniuyunKodoFileHandler(properties.getKodo());
    }

    /**
     * 腾讯云对象存储 COS
     *
     * @param properties 文件存储配置信息
     * @return TencentyunCosFileHandler
     */
    @Bean("TencentyunCosFileHandler")
    @ConditionalOnProperty(prefix = "zblog.base.file.cos", name = "enabled", havingValue = "true")
    public FileHandler tencentyunCosFileHandler(FileProperties properties) {
        return new TencentyunCosFileHandler(properties.getCos());
    }
}
