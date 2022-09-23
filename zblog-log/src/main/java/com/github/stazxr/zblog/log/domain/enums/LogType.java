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
     * 操作日志
     */
    OPERATE(1),

    /**
     * 接口日志
     */
    API(2),

    /**
     * 异常日志
     */
    ERROR(3);

    private final int value;

    LogType(int value) {
        this.value = value;
    }

    public static String of(Integer value) {
        switch (value) {
            case 1:
                return "操作日志";
            case 2:
                return "接口日志";
            case 3:
                return "异常日志";
            default:
                return "";
        }
    }
}
