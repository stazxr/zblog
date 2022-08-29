package com.github.stazxr.zblog.base.domain.enums;

/**
 * 接口类型
 *
 * @author SunTao
 * @since 2022-07-12
 */
public enum InterfaceType {
    /**
     * 受权限管控的接口
     */
    PERM(1),

    /**
     * 不可以访问的接口
     */
    NULL(2);

    private final Integer type;

    InterfaceType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
