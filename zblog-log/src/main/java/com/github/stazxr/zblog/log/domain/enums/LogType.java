package com.github.stazxr.zblog.log.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 日志类型
 *
 * @author SunTao
 * @since 2022-06-20
 */
public enum LogType {
    /**
     * 操作日志
     */
    INFO(1),

    /**
     * 异常日志
     */
    ERROR(2);

    @EnumValue
    private final Integer value;

    LogType(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return value;
    }
}
