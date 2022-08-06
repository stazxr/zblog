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
    INFO(1),

    /**
     * 异常日志
     */
    ERROR(2);

    private final Integer value;

    LogType(Integer value) {
        this.value = value;
    }
}
