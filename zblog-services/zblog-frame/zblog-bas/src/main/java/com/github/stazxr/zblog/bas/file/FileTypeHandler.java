package com.github.stazxr.zblog.bas.file;

import com.github.stazxr.zblog.bas.context.util.SpringContextUtil;
import lombok.Getter;

/**
 * 文件存储类型
 *
 * @author SunTao
 * @since 2022-07-29
 */
public enum FileTypeHandler {
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
                return SpringContextUtil.getBean(fileTypeHandler.beanName, FileHandler.class);
            }
        }

        throw new FileException("Not support: " + fileUploadType);
    }

    public static FileHandler of(int fileUploadType) {
        for (FileTypeHandler fileTypeHandler : values()) {
            if (fileTypeHandler.type == fileUploadType) {
                return SpringContextUtil.getBean(fileTypeHandler.beanName, FileHandler.class);
            }
        }

        throw new FileException("Not support: " + fileUploadType);
    }

    public static String ofName(int fileUploadType) {
        for (FileTypeHandler fileTypeHandler : values()) {
            if (fileTypeHandler.type == fileUploadType) {
                return fileTypeHandler.name;
            }
        }

        throw new FileException("Not support: " + fileUploadType);
    }
}
