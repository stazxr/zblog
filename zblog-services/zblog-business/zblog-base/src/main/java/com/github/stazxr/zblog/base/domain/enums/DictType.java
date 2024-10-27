package com.github.stazxr.zblog.base.domain.enums;

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

    public Integer getValue() {
        return value;
    }

    public static DictType of(Integer type) {
        for (DictType dictType : values()) {
            if (dictType.value.equals(type)) {
                return dictType;
            }
        }

        return null;
    }
}
