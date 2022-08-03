package com.github.stazxr.zblog.base.third.file;

import com.github.stazxr.zblog.core.exception.BadConfigurationException;
import com.github.stazxr.zblog.core.util.SpringContextUtils;

/**
 * 文件上传类型处理
 *
 * @author SunTao
 * @since 2022-07-29
 */
public enum FileTypeHandler {
    /**
     * 本地存储
     */
    LOCAL(1, "LocalFileHandlerService"),

    /**
     * 阿里云存储
     */
    ALI_YUN(2, "AliYunFileHandlerService"),

    /**
     * 七牛云存储
     */
    QI_NIU_YUN(3, "QiNiuYunFileHandlerService");

    private final int type;
    private final String beanName;

    FileTypeHandler(int type, String beanName) {
        this.type = type;
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
}
