package com.github.stazxr.zblog.base.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * 角色权限
 *
 * @author SunTao
 * @since 2022-09-13
 */
@Data
@ApiModel("角色授权参数-权限")
public class RolePermDto {
    /**
     * 权限id
     */
    @ApiModelProperty("权限id")
    private Long permId;

    /**
     * 角色id列表
     */
    @ApiModelProperty("角色id列表")
    private Set<Long> roleIds;
}
