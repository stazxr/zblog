package com.github.stazxr.zblog.base.domain.vo;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 左侧菜单渲染
 *
 * @author SunTao
 * @since 2022-06-17
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MenuVo implements Serializable {
    private static final long serialVersionUID = 1937699132190889664L;

    /**
     * 路由名称
     */
    private String name;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 菜单是否显示
     */
    private Boolean show;

    /**
     * 当目录下只有一个子菜单时，是否强制显示目录
     */
    private Boolean alwaysShow;

    /**
     * 是否支持重定向（跳转），"noredirect" 代表不进行重定向
     *
     * 前端面包屑和路由检索功能使用
     */
    private String redirect;

    /**
     * 组件路径
     *  <li>Layout</li>
     *  <li>ParentView</li>
     *  <li>return (resolve) => require([`@/views/${component}`], resolve)</li>
     */
    private String component;

    /**
     * 菜单元数据（菜单标题，菜单图标，是否展示等）
     */
    private MenuMetaVo meta;

    /**
     * 子菜单
     */
    private List<MenuVo> children;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
