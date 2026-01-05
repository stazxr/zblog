package com.github.stazxr.zblog.bas.file.autoconfigure;

import com.github.stazxr.zblog.bas.file.FileHandler;
import com.github.stazxr.zblog.bas.file.impl.LocalFileHandlerService;
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
     * @param properties 文件上传下载配置信息
     * @return LocalFileHandlerService
     */
    @Bean("LocalFileHandlerService")
    @ConditionalOnProperty(name = "zblog.base.file.model", havingValue = "1")
    public FileHandler localFileHandlerService(FileProperties properties) {
        FileProperties.LocalConfig local = properties.getLocal();
        return new LocalFileHandlerService(local.getBaseUrl(), local.getFileUploadPath());
    }

//    /**
//     * 本地文件存储
//     *
//     * @param properties 文件上传下载配置信息
//     * @return LocalFileHandlerService
//     */
//    @Bean
//    @ConditionalOnProperty(name = "zblog.base.file.model", havingValue = "2")
//    public FileHandler localFileHandlerService(FileProperties properties) {
//        FileProperties.LocalConfig local = properties.getLocal();
//        return new LocalFileHandlerService(local.getBaseUrl(), local.getFileUploadPath());
//    }
//
//    /**
//     * 本地文件存储
//     *
//     * @param properties 文件上传下载配置信息
//     * @return LocalFileHandlerService
//     */
//    @Bean
//    @ConditionalOnProperty(name = "zblog.base.file.model", havingValue = "3")
//    public FileHandler localFileHandlerService(FileProperties properties) {
//        FileProperties.LocalConfig local = properties.getLocal();
//        return new LocalFileHandlerService(local.getBaseUrl(), local.getFileUploadPath());
//    }
}
