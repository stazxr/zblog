package com.github.stazxr.zblog.base.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 权限级别
 *
 * @author SunTao
 * @since 2020-11-28
 */
public enum PermissionLevel {
    /**
     * 开放权限（可直接访问）
     */
    OPEN(1),

    /**
     * 公共权限（登录即可访问）
     */
    PUBLIC(2),

    /**
     * 受控权限（需要对应角色才可以访问）
     */
    CONTROLLED(3);

    @EnumValue
    private final Integer level;

    PermissionLevel(Integer level) {
        this.level = level;
    }

    public Integer value() {
        return level;
    }
}
