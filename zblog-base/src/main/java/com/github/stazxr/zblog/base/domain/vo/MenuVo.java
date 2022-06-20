package com.github.stazxr.zblog.base.domain.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 前端路由菜单自动渲染
 *
 * @author SunTao
 * @since 2022-06-17
 */
@Data
@ToString
public class MenuVo {
    /**
     * 菜单名称
     */
    private String title;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 子节点
     */
    private List<MenuVo> children;
}
