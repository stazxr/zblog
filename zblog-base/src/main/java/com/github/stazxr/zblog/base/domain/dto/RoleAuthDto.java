package com.github.stazxr.zblog.base.domain.dto;

import lombok.Data;

import java.util.Set;

/**
 * 角色授权
 *
 * @author SunTao
 * @since 2022-08-30
 */
@Data
public class RoleAuthDto {
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 权限ID列表
     */
    private Set<Long> permIds;
}
