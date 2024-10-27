package com.github.stazxr.zblog.base.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * PermissionVo
 *
 * @author SunTao
 * @since 2022-06-30
 */
@Data
public class PermissionVo {
    /**
     * ID
     */
    private Long id;

    /**
     * ID
     */
    private Long pid;

    /**
     * 菜单标题
     */
    private String permName;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 权限标识
     */
    private String permCode;

    /**
     * 路由地址
     */
    private String routerPath;

    /**
     * 组件名称
     */
    private String componentName;

    /**
     * 组件路径
     */
    private String componentPath;

    /**
     * 权限类型
     */
    private Integer permType;

    /**
     * 访问级别
     */
    private Integer permLevel;

    /**
     * 是否外链
     */
    private Boolean iFrame;

    /**
     * 是否缓存
     */
    private Boolean cache;

    /**
     * 是否隐藏
     */
    private Boolean hidden;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改用户
     */
    private String updateUser;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 是否包含子节点，懒加载用
     */
    private Boolean hasChildren;

    /**
     * 子节点列表，非懒加载用
     */
    private List<PermissionVo> children;
}
