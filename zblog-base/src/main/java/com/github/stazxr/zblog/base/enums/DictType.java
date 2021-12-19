package com.github.stazxr.zblog.base.enums;

/**
 * 字典类型
 *
 * @author SunTao
 * @since 2021-10-05
 */
public enum DictType {
    /**
     * 组
     */
    GROUP(1),

    /**
     * 项
     */
    ITEM(2);

    private final Integer value;

    DictType(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return value;
    }
}
