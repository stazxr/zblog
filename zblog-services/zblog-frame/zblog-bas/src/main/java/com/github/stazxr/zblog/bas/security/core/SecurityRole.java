package com.github.stazxr.zblog.bas.security.core;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

/**
 * SecurityRole 类实现了 Spring Security 的 GrantedAuthority 接口，
 * 用于表示用户的角色或权限信息。
 * <p>
 * 该类包含角色代码，用于标识用户的权限角色。
 * 实现了 {@link GrantedAuthority} 接口的 {@code getAuthority()} 方法，
 * 返回角色代码作为权限字符串。
 * </p>
 *
 * @author SunTao
 * @since 2024-11-10
 */
public class SecurityRole implements GrantedAuthority, Serializable {
    private static final long serialVersionUID = 1867404593420585268L;

    /**
     * 角色代码，标识用户的权限角色
     */
    private String roleCode;

    /**
     * 角色是否启用
     */
    private boolean enabled;

    /**
     * 获取该角色的权限字符串，返回角色代码。
     *
     * @return 角色代码
     */
    @Override
    public String getAuthority() {
        return roleCode;
    }

    /**
     * 获取角色编码
     *
     * @return 角色编码
     */
    public String getRoleCode() {
        return roleCode;
    }

    /**
     * 设置角色代码。
     *
     * @param roleCode 角色代码
     */
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    /**
     * 获取该角色的状态。
     *
     * @return 角色状态
     */
    public Boolean isEnabled() {
        return enabled;
    }

    /**
     * 设置角色状态。
     *
     * @param enabled 角色状态
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return roleCode;
    }
}
