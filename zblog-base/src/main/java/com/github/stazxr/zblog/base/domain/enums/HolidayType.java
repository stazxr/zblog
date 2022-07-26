package com.github.stazxr.zblog.base.domain.enums;

/**
 * 节假日类型
 *
 * @author SunTao
 * @since 2022-04-11
 */
public enum HolidayType {
    /**
     * 节假日
     */
    HOLIDAY("0"),

    /**
     * 工作日
     */
    WORKDAY("1");

    private final String type;

    HolidayType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
