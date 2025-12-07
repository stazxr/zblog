package com.github.stazxr.zblog.base.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * PermissionVo
 *
 * @author SunTao
 * @since 2022-06-30
 */
@Getter
@Setter
public class PermissionVo extends BaseVo {
    private static final long serialVersionUID = 1093135022712327296L;

    /**
     * 权限ID
     */
    private Long id;

    /**
     * 上级权限ID
     */
    private Long pid;

    /**
     * 权限名称
     */
    private String permName;

    /**
     * 权限编码
     */
    private String permCode;

    /**
     * 权限类型
     */
    private Integer permType;

    /**
     * 权限访问级别
     */
    private Integer permLevel;

    /**
     * 组件名称
     */
    private String componentName;

    /**
     * 组件路径
     */
    private String componentPath;

    /**
     * 路由地址
     */
    private String routerPath;

    /**
     * 权限图标
     */
    private String icon;

    /**
     * 权限排序
     */
    private Integer sort;

    /**
     * 是否缓存
     */
    private Boolean cacheable;

    /**
     * 是否隐藏
     */
    private Boolean hidden;

    /**
     * 是否外链
     */
    private Boolean iFrame;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 是否包含子节点，懒加载用
     */
    private Boolean hasChildren;

    /**
     * 子节点列表，非懒加载用
     */
    private List<PermissionVo> children;

    /**
     * 角色编码列表
     */
    private List<String> roleCodeList;
}
