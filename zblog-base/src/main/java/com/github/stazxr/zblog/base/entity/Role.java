package com.github.stazxr.zblog.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.entity.BaseEntity;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * 角色
 *
 * @author SunTao
 * @since 2020-11-15
 */
@TableName("sys_role")
@Getter
@Setter
public class Role extends BaseEntity {
    /**
     * serialId
     */
    private static final long serialVersionUID = -5973469445336539812L;

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    @TableField(value = "`DESC`")
    private String desc;

    /**
     * 角色状态
     */
    private Boolean active;

    /**
     * 角色是否是内置的
     */
    private Boolean buildIn;

    /**
     * 是否已删除（逻辑操作，保护数据）
     */
    @TableLogic
    private Boolean deleted;

    /**
     * 权限列表
     */
    @TableField(exist = false)
    private List<Permission> permissions;

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
        return !StringUtils.isEmpty(roleCode) && roleCode.equalsIgnoreCase(other.getRoleCode());
    }

    @Override
    public String toString() {
        return "Role{" +
                "id='" + getId() + '\'' +
                ", roleCode='" + roleCode + '\'' +
                ", roleName='" + roleName + '\'' +
                ", desc='" + desc + '\'' +
                ", active=" + active +
                ", buildIn=" + buildIn +
                '}';
    }
}
