package com.github.stazxr.zblog.base.component.file.model;

import lombok.Getter;

/**
 * 文件上传类型
 *
 * @author SunTao
 * @since 2022-10-22
 */
public enum FileUploadType {
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

    FileUploadType(String type) {
        this.type = type;
    }
}
