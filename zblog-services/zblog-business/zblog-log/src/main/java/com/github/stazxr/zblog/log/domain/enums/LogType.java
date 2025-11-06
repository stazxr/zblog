package com.github.stazxr.zblog.log.domain.enums;

import lombok.Getter;

/**
 * 日志类型
 *
 * @author SunTao
 * @since 2022-06-20
 */
@Getter
public enum LogType {
    /**
     * 接口日志
     */
    INTERFACES(1),

    /**
     * 操作日志
     */
    OPERATION(2);

    private final int value;

    LogType(int value) {
        this.value = value;
    }

    public static String of(Integer value) {
        switch (value) {
            case 1:
                return "接口日志";
            case 2:
                return "操作日志";
            default:
                return null;
        }
    }
}
