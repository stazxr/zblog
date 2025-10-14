package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.bas.security.core.SecurityRole;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * 系统角色
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Getter
@Setter
@TableName("role")
public class Role extends SecurityRole {
    private static final long serialVersionUID = 4908839100794920133L;

    /**
     * 角色id
     */
    @TableId
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 是否有效
     */
    private Boolean deleted;

    /**
     * 创建用户，存储该实体的创建用户标识。
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间，存储该实体的创建时间。
     */
    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    /**
     * 修改用户，存储最后修改该实体的用户标识。
     */
    @TableField(fill = FieldFill.UPDATE)
    private Long updateUser;

    /**
     * 修改时间，存储该实体的最后修改时间。
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updateTime;

    @Override
    public int hashCode() {
        return Objects.hash(getRoleCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Role other = (Role) obj;
        return !StringUtils.isEmpty(getRoleCode()) && getRoleCode().equalsIgnoreCase(other.getRoleCode());
    }

    @Override
    public String toString() {
        return getAuthority();
    }
}
