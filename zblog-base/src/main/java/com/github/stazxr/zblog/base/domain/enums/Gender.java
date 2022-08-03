package com.github.stazxr.zblog.base.domain.enums;

import lombok.Getter;

/**
 * 性别
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Getter
public enum Gender {
    /**
     * 男
     */
    MAIL(1),

    /**
     * 女
     */
    FEMALE(2),

    /**
     * 隐藏
     */
    HIDE(3);

    private final Integer type;

    Gender(Integer type) {
        this.type = type;
    }

    public static Gender of(Integer genderType) {
        for (Gender gender : values()) {
            if (gender.type.equals(genderType)) {
                return gender;
            }
        }

        return null;
    }
}
