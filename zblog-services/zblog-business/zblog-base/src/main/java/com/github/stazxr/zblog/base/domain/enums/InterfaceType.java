package com.github.stazxr.zblog.base.domain.enums;

/**
 * 接口类型
 *
 * @author SunTao
 * @since 2022-07-12
 */
public enum InterfaceType {
    /**
     * Router注解标注的接口
     */
    PERM(1),

    /**
     * 未被Router注解标注的接口，不允许访问
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
