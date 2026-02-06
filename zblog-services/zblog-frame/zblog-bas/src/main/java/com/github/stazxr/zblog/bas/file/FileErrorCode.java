package com.github.stazxr.zblog.bas.file;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 文件错误码定义。
 *
 * @author SunTao
 * @since 2026-01-25
 */
public enum FileErrorCode implements ErrorCode {
    /**
     * {0}存储功能未启用
     */
    EFILEA000("error.service.file.disabled"),

    /**
     * 存储参数配置错误
     */
    SFILEA000("error.system.file.config"),

    /**
     * 文件上传失败
     */
    SFILEA001("error.system.file.upload"),

    /**
     * 文件下载失败
     */
    SFILEA002("error.system.file.download"),

    /**
     * 文件删除失败
     */
    SFILEA003("error.system.file.delete"),
    ;

    private final String i18nKey;

    FileErrorCode(String i18nKey) {
        this.i18nKey = i18nKey;
    }


    /**
     * 错误码
     */
    @Override
    public String getCode() {
        return name();
    }

    /**
     * 国际化 key，用于获取对应消息
     *
     * @return i18n key
     */
    @Override
    public String getI18nKey() {
        return i18nKey;
    }
}
