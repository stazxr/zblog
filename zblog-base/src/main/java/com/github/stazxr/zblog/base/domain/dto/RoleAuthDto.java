package com.github.stazxr.zblog.base.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * 角色授权
 *
 * @author SunTao
 * @since 2022-08-30
 */
@Data
@ApiModel("角色授权参数-角色")
public class RoleAuthDto {
    /**
     * 角色ID
     */
    @ApiModelProperty("角色id")
    private Long roleId;

    /**
     * 权限id列表
     */
    @ApiModelProperty("权限id列表")
    private Set<Long> permIds;
}
