package com.github.stazxr.zblog.base.component.file;

import com.github.stazxr.zblog.core.exception.BadConfigurationException;
import com.github.stazxr.zblog.core.util.SpringContextUtils;
import lombok.Getter;

/**
 * 文件存储类型
 *
 * @author SunTao
 * @since 2022-07-29
 */
public enum FileTypeHandler {
    /**
     * 默认，已配置文件为主
     */
    DEFAULT(0, "默认", ""),

    /**
     * 本地存储
     */
    LOCAL(1, "本地", "LocalFileHandlerService"),

    /**
     * 阿里云存储
     */
    ALI_YUN(2, "阿里云", "AliYunFileHandlerService"),

    /**
     * 七牛云存储
     */
    QI_NIU_YUN(3, "七牛云", "QiNiuYunFileHandlerService");

    @Getter
    private final int type;

    private final String name;

    private final String beanName;

    FileTypeHandler(int type, String name, String beanName) {
        this.type = type;
        this.name = name;
        this.beanName = beanName;
    }

    public static FileHandler instance(int fileUploadType) {
        for (FileTypeHandler fileTypeHandler : values()) {
            if (fileTypeHandler.type == fileUploadType) {
                return SpringContextUtils.getBean(fileTypeHandler.beanName, FileHandler.class);
            }
        }

        throw new BadConfigurationException("不支持的存储类型");
    }

    public static FileHandler of(int fileUploadType) {
        for (FileTypeHandler fileTypeHandler : values()) {
            if (fileTypeHandler.type == fileUploadType) {
                return SpringContextUtils.getBean(fileTypeHandler.beanName, FileHandler.class);
            }
        }

        throw new BadConfigurationException("不支持的存储类型");
    }

    public static String ofName(int fileUploadType) {
        for (FileTypeHandler fileTypeHandler : values()) {
            if (fileTypeHandler.type == fileUploadType) {
                return fileTypeHandler.name;
            }
        }

        return "不支持的存储类型";
    }
}
