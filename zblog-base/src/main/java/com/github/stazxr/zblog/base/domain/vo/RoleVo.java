package com.github.stazxr.zblog.base.domain.vo;

import lombok.Data;

/**
 * RoleVo
 *
 * @author SunTao
 * @since 2022-08-29
 */
@Data
public class RoleVo {
    /**
     * ID
     */
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
    private String desc;

    /**
     * 角色状态
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
}
