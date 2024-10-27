package com.github.stazxr.zblog.bas.file.model;

import lombok.Getter;

/**
 * 上传文件类型
 *
 * @author SunTao
 * @since 2022-10-22
 */
public enum UploadFileType {
    /**
     * 普通文件
     */
    NORMAL("1"),

    /**
     * 测试文件
     */
    TEST("2");

    @Getter
    private final String type;

    UploadFileType(String type) {
        this.type = type;
    }
}
