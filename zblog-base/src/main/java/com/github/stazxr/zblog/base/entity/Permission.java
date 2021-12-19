package com.github.stazxr.zblog.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.github.stazxr.zblog.base.enums.PermissionLevel;
import com.github.stazxr.zblog.base.enums.PermissionType;
import com.github.stazxr.zblog.core.base.entity.BaseEntity;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * 权限
 *
 * @author SunTao
 * @since 2020-11-15
 */
@TableName("sys_permission")
@Getter
@Setter
public class Permission extends BaseEntity {
    /**
     * serialId
     */
    private static final long serialVersionUID = -1866426336122900009L;

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 权限名称
     */
    @TableField(value = "`NAME`")
    private String name;

    /**
     * 权限 URL
     */
    private String url;

    /**
     * 权限标识
     */
    private String perm;

    /**
     * 父权限ID
     */
    private Long parentId;

    /**
     * 权限类型
     */
    private PermissionType type;

    /**
     * 权限级别
     */
    @TableField(value = "`LEVEL`")
    private PermissionLevel level;

    /**
     * 权限图标
     */
    private String icon;

    /**
     * 排序字段
     */
    @TableField(value = "`ORDER`")
    private Long order;

    /**
     * 权限状态（正常/禁用）
     */
    private Boolean active;

    /**
     * 是否已删除（逻辑操作，保护数据）
     */
    @TableLogic
    private Boolean deleted;

    /**
     * 子权限列表
     */
    @TableField(exist = false)
    private List<Permission> childrenList;

    @Override
    public String toString() {
        return "Permission{" +
                "id='" + getId() + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", perm='" + perm + '\'' +
                ", parentId=" + parentId +
                ", type=" + type +
                ", level=" + level +
                ", icon='" + icon + '\'' +
                ", order=" + order +
                ", active=" + active +
                ", version=" + getVersion() +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
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
        return !StringUtils.isEmpty(name) && name.equalsIgnoreCase(other.getName());
    }
}
