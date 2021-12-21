package com.github.stazxr.zblog.base.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 权限类型
 *
 * @author SunTao
 * @since 2020-11-15
 */
public enum PermissionType {
    /**
     * 目录，可分配URL
     */
    DIR(1),

    /**
     * 菜单，无URL
     */
    MENU(2),

    /**
     * 按钮（页面的增加/删除等按钮）
     */
    BTN(3);

    @EnumValue
    private final Integer type;

    PermissionType(Integer type) {
        this.type = type;
    }

    public Integer value() {
        return type;
    }
}
