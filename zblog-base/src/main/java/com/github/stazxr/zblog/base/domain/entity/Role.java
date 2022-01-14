package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

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
public class Role extends BaseEntity implements GrantedAuthority {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色描述
     */
    @TableField(value = "`DESC`")
    private String desc;

    /**
     * 角色状态
     */
    private Boolean enabled;

    /**
     * 是否有效
     */
    @TableLogic
    private Boolean deleted;

    /**
     * 返回角色编码
     *
     * @return roleCode
     */
    @Override
    public String getAuthority() {
        return roleCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleCode);
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
        return getAuthority();
    }
}
