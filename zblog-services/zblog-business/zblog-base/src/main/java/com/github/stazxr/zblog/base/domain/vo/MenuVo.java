package com.github.stazxr.zblog.base.domain.vo;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.stazxr.zblog.base.domain.bo.MenuMeta;
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
    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 是否隐藏
     */
    private Boolean hidden;

    /**
     * 是否重定向
     */
    private String redirect;

    /**
     * 组件路径
     *  Layout
     *  ParentView
     *  return (resolve) => require([`@/views/${component}`], resolve)
     */
    private String component;

    /**
     * 是否展示
     */
    private Boolean alwaysShow;

    /**
     * 基础数据
     */
    private MenuMeta meta;

    /**
     * 子菜单
     */
    private List<MenuVo> children;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
