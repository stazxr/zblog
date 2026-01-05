package com.github.stazxr.zblog.bas.file;

import com.github.stazxr.zblog.bas.context.util.SpringContextUtil;
import com.github.stazxr.zblog.bas.exception.ExpMessageCode;
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
    LOCAL(1, "本地", "LocalFileHandlerService"),

    /**
     * 阿里云存储
     */
    ALI_YUN(2, "阿里云", "AliYunFileHandlerService"),

    /**
     * 七牛云存储
     */
    QI_NIU_YUN(3, "七牛云", "QiNiuYunFileHandlerService");

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
                    return SpringContextUtil.getBean(fileHandlerEnum.beanName, FileHandler.class);
                } catch (NoSuchBeanDefinitionException e) {
                    throw new FileException(ExpMessageCode.of("valid.file.bas.beanNotFound", fileHandlerEnum.beanName));
                }
            }
        }

        throw new FileException(ExpMessageCode.of("valid.file.bas.typeNotSupport", fileUploadType));
    }
}
