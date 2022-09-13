package com.github.stazxr.zblog.base.domain.dto;

import lombok.Data;

import java.util.Set;

/**
 * 用户角色
 *
 * @author SunTao
 * @since 2022-09-13
 */
@Data
public class UserRoleDto {
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 用户ID列表
     */
    private Set<Long> userIds;
}
