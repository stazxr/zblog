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
     * TODO 菜单名称
     */
    private String name;

    /**
     * TODO 菜单路径
     */
    private String path;

    /**
     * 菜单是否显示
     */
    private Boolean show;

    /**
     * TODO 是否展示
     */
    private Boolean alwaysShow;

    /**
     * 重定向路径，"noredirect" 代表不允许重定向
     */
    private String redirect;

    /**
     * TODO 组件路径
     *  Layout
     *  ParentView
     *  return (resolve) => require([`@/views/${component}`], resolve)
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
