package com.github.stazxr.zblog.core.base.impl;

import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 扩展 BaseEntity，增加扩展字段
 *
 * @author SunTao
 * @since 2022-06-23
 */
@Getter
@Setter
public class BaseEntityWithExtend extends BaseEntity {
    /**
     * 扩展字段1
     */
    private String extend1;

    /**
     * 扩展字段2
     */
    private String extend2;

    /**
     * 扩展字段3
     */
    private String extend3;

    /**
     * 扩展字段4
     */
    private String extend4;

    /**
     * 扩展字段5
     */
    private String extend5;

    @Override
    public String toString() {
        return super.toString();
    }
}
