package com.github.stazxr.zblog.base.domain.dto;

import com.github.stazxr.zblog.bas.validation.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * 角色授权
 *
 * @author SunTao
 * @since 2022-08-30
 */
@Getter
@Setter
@ApiModel("角色授权信息")
public class RoleAuthDto {
    /**
     * 角色id
     */
    @NotNull(message = "{valid.role.id.NotNull}")
    @ApiModelProperty("角色id")
    private Long roleId;

    /**
     * 权限id列表
     */
    @ApiModelProperty("权限id列表")
    private Set<Long> permIds;
}
