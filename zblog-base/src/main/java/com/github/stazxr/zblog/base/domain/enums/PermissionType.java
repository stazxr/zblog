package com.github.stazxr.zblog.base.domain.enums;

import lombok.Getter;

/**
 * 权限类型
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Getter
public enum PermissionType {
    /**
     * 目录
     */
    DIR(1),

    /**
     * 菜单
     */
    MENU(2),

    /**
     * 按钮
     */
    BTN(3);

    private final Integer type;

    PermissionType(Integer type) {
        this.type = type;
    }
}
