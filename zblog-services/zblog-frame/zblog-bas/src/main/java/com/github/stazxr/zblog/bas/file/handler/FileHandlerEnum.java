package com.github.stazxr.zblog.bas.file.handler;

import com.github.stazxr.zblog.bas.context.util.SpringContextHolder;
import com.github.stazxr.zblog.bas.file.FileErrorCode;
import com.github.stazxr.zblog.bas.file.FileException;
import lombok.Getter;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * 文件存储类型
 *
 * @author SunTao
 * @since 2022-07-29
 */
@Getter
public enum FileHandlerEnum {
    /**
     * 本地存储
     */
    LOCAL(1, "本地", "LocalFileHandler"),

    /**
     * 阿里云 OSS 存储
     */
    ALIYUN(2, "阿里云", "AliyunOssFileHandler"),

    /**
     * 七牛云  存储
     */
    QINIUYUN(3, "七牛云", "QiniuyunKodoFileHandler"),

    /**
     * 腾讯云 COS 存储
     */
    TENCENTYUN(4, "腾讯云", "TencentyunCosFileHandler");

    private final int type;

    private final String name;

    private final String beanName;

    FileHandlerEnum(int type, String name, String beanName) {
        this.type = type;
        this.name = name;
        this.beanName = beanName;
    }

    public static FileHandler instance(int fileUploadType) {
        for (FileHandlerEnum fileHandlerEnum : values()) {
            if (fileHandlerEnum.type == fileUploadType) {
                try {
                    return SpringContextHolder.getBean(fileHandlerEnum.beanName, FileHandler.class);
                } catch (NoSuchBeanDefinitionException e) {
                    throw new FileException(FileErrorCode.EFILEA000, fileHandlerEnum.beanName);
                }
            }
        }

        throw new FileException(FileErrorCode.SFILEA000, fileUploadType);
    }
}
