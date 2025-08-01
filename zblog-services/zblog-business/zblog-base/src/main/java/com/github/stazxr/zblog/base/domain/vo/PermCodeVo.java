package com.github.stazxr.zblog.base.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 权限编码实体
 *
 * @author SunTao
 * @since 2022-08-26
 */
@Getter
@Setter
public class PermCodeVo {
    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限编码
     */
    private String value;

    /**
     * 访问级别（默认）
     */
    private Integer level;

    /**
     * 是否禁用（select）
     */
    private Boolean disabled;
}
