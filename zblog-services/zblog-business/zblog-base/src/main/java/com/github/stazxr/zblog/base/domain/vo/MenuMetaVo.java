package com.github.stazxr.zblog.base.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 左侧菜单元数据
 *
 * @author SunTao
 * @since 2022-06-30
 */
@Getter
@Setter
public class MenuMetaVo implements Serializable {
    private static final long serialVersionUID = 3496417467290425962L;

    public MenuMetaVo() {
    }

    public MenuMetaVo(String title, String icon, Boolean cache, Boolean breadcrumb) {
        this.title = title;
        this.icon = icon;
        this.cache = cache;
        this.breadcrumb = breadcrumb;
    }

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 是否缓存（前端缓存）
     */
    private Boolean cache;

    /**
     * 是否展示面包屑
     */
    private Boolean breadcrumb;

    /**
     * 是否固定在标签栏，默认 false
     */
    private Boolean affix;
}
