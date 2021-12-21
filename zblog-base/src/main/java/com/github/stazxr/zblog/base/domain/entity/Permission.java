package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.github.stazxr.zblog.base.domain.enums.PermissionLevel;
import com.github.stazxr.zblog.base.domain.enums.PermissionType;
import com.github.stazxr.zblog.core.base.BaseEntity;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * 权限
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Getter
@Setter
@TableName("sys_permission")
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
    private String permUrl;

    /**
     * 权限标识
     */
    private String permCode;

    /**
     * 父权限ID
     */
    private Long pid;

    /**
     * 权限类型
     */
    private PermissionType permType;

    /**
     * 权限级别
     */
    private PermissionLevel permLevel;

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

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", permUrl='" + permUrl + '\'' +
                ", permCode='" + permCode + '\'' +
                ", pid=" + pid +
                ", permType=" + permType +
                ", permLevel=" + permLevel +
                ", icon='" + icon + '\'' +
                ", order=" + order +
                ", active=" + active +
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
