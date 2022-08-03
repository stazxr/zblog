package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.stazxr.zblog.base.domain.enums.PermissionType;
import com.github.stazxr.zblog.core.base.BaseEntity;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * 系统权限
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Getter
@Setter
@TableName("permission")
public class Permission extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 父权限ID, top perm is null
     */
    private Long pid;

    /**
     * 子节点数目
     */
    private Integer subCount;

    /**
     * 目录名称/菜单名称/权限名称
     */
    private String permName;

    /**
     * 权限类型
     * {@link com.github.stazxr.zblog.base.domain.enums.PermissionType}
     */
    private Integer permType;

    /**
     * 权限编码（应与路由中的编码保持一致，否则找不到权限可以访问的接口列表）
     */
    private String permCode;

    /**
     * 权限访问级别
     * {@link com.github.stazxr.zblog.core.annotation.Router#level()}
     */
    private Integer permLevel;

    /**
     * 组件名称
     * if {@link PermissionType#DIR} be null
     * if {@link PermissionType#MENU} be vue component name
     * if {@link PermissionType#BTN} be null
     */
    private String componentName;

    /**
     * 组件路径(前端一般对应位view下的路径, eg: account/user/index)
     * if {@link PermissionType#DIR} be null
     * if {@link PermissionType#MENU} be vue component path
     * if {@link PermissionType#BTN} be null
     */
    private String componentPath;

    /**
     * 前端路由地址
     * if {@link PermissionType#DIR} be null
     * if {@link PermissionType#MENU} be vue menu path or http url (if iframe)
     * if {@link PermissionType#BTN} be null
     */
    private String routerPath;

    /**
     * 权限图标
     * if {@link PermissionType#BTN} be empty string
     */
    private String icon;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 是否缓存
     * only for {@link PermissionType#MENU}
     */
    private Boolean cache;

    /**
     * 是否隐藏
     * if {@link PermissionType#BTN} be null
     */
    private Boolean hidden;

    /**
     * 是否外链
     * if {@link PermissionType#BTN} be null
     */
    private Boolean iFrame;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 是否有效
     */
    @TableLogic
    private Boolean deleted;

    /**
     * 子菜单
     */
    @JsonIgnore
    @TableField(exist = false)
    private List<Permission> children;

    @Override
    public int hashCode() {
        return Objects.hash(getPermName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Permission other = (Permission) obj;
        return !StringUtils.isEmpty(permName) && permName.equalsIgnoreCase(other.getPermName());
    }
}
