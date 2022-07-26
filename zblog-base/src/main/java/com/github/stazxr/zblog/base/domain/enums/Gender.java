package com.github.stazxr.zblog.base.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 性别
 *
 * @author SunTao
 * @since 2020-11-15
 */
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

    @EnumValue
    private final Integer sex;

    Gender(Integer sex) {
        this.sex = sex;
    }

    public Integer getSex() {
        return sex;
    }
}
