package com.github.stazxr.zblog.base.domain.dto;

import lombok.Data;

import java.util.Set;

/**
 * 角色权限
 *
 * @author SunTao
 * @since 2022-09-13
 */
@Data
public class RolePermDto {
    /**
     * 权限ID
     */
    private Long permId;

    /**
     * 角色ID列表
     */
    private Set<Long> roleIds;
}
