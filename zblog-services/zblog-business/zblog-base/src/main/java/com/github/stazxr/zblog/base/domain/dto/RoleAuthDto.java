package com.github.stazxr.zblog.base.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * 角色授权信息
 *
 * @author SunTao
 * @since 2022-08-30
 */
@Getter
@Setter
@ApiModel("角色授权DTO")
public class RoleAuthDto implements Serializable {
    private static final long serialVersionUID = -277112550492354876L;

    /**
     * 角色id
     */
    @NotNull(message = "{valid.common.id.required}")
    @ApiModelProperty("角色id")
    private Long roleId;

    /**
     * 权限id列表
     */
    @ApiModelProperty("权限id列表")
    private Set<Long> permIds;
}
