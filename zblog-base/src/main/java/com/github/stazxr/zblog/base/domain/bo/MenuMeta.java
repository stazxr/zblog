package com.github.stazxr.zblog.base.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 左侧菜单元数据
 *
 * @author SunTao
 * @since 2022-06-30
 */
@Data
@AllArgsConstructor
public class MenuMeta implements Serializable {
    /**
     * 菜单标题
     */
    private String title;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 是否缓存
     */
    private Boolean noCache;
}
