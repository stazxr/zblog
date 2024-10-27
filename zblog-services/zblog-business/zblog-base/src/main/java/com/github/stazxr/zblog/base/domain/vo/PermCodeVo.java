package com.github.stazxr.zblog.base.domain.vo;

import lombok.Data;

/**
 * 权限编码实体
 *
 * @author SunTao
 * @since 2022-08-26
 */
@Data
public class PermCodeVo {
    /**
     * 路由名称
     */
    private String name;

    /**
     * 权限编码
     */
    private String value;

    /**
     * 权限默认级别
     */
    private Integer permLevel;

    /**
     * 是否禁用（select）
     */
    private Boolean disabled;
}
