package com.github.stazxr.zblog.base.domain.vo;

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
public class MenuMetaVo implements Serializable {
    private static final long serialVersionUID = 3496417467290425962L;

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
}
