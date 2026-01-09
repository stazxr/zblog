package com.github.stazxr.zblog.bas.file.autoconfigure;

import com.github.stazxr.zblog.bas.file.FileHandler;
import com.github.stazxr.zblog.bas.file.impl.AliyunFileHandlerService;
import com.github.stazxr.zblog.bas.file.impl.LocalFileHandlerService;
import com.github.stazxr.zblog.bas.file.impl.QiniuyunFileHandlerService;
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
     * @return LocalFileHandlerService
     */
    @Bean("LocalFileHandlerService")
    @ConditionalOnProperty(prefix = "zblog.base.file.local", name = "enabled", havingValue = "true")
    public FileHandler localFileHandlerService(FileProperties properties) {
        return new LocalFileHandlerService(properties.getLocal());
    }

    /**
     * 阿里云对象存储 OSS
     *
     * @param properties 文件存储配置信息
     * @return AliyunFileHandlerService
     */
    @Bean("AliyunFileHandlerService")
    @ConditionalOnProperty(prefix = "zblog.base.file.aliyun", name = "enabled", havingValue = "true")
    public FileHandler aliyunFileHandlerService(FileProperties properties) {
        return new AliyunFileHandlerService(properties.getAliyun());
    }

    /**
     * 七牛云文件存储 Kodo
     *
     * @param properties 文件存储配置信息
     * @return QiniuyunFileHandlerService
     */
    @Bean("QiniuyunFileHandlerService")
    @ConditionalOnProperty(prefix = "zblog.base.file.qiniuyun", name = "enabled", havingValue = "true")
    public FileHandler qiniuyunFileHandlerService(FileProperties properties) {
        return new QiniuyunFileHandlerService(properties.getQiniuyun());
    }
}
