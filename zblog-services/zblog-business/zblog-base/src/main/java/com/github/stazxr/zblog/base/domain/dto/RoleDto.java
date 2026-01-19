package com.github.stazxr.zblog.base.domain.dto;

import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.core.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 角色信息
 *
 * @author SunTao
 * @since 2025-09-13
 */
@Getter
@Setter
@ApiModel("角色DTO")
public class RoleDto extends BaseDto {
    private static final long serialVersionUID = 5704217272490958998L;

    /**
     * 角色id
     */
    @NotNull(groups = Update.class, message = "{valid.common.id.required}")
    @ApiModelProperty(value = "角色id")
    private Long id;

    /**
     * 角色名称
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{valid.role.roleName.NotBlank}")
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    /**
     * 角色编码
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{valid.role.roleCode.NotBlank}")
    @ApiModelProperty(value = "角色编码")
    private String roleCode;

    /**
     * 角色描述
     */
    @ApiModelProperty(value = "角色描述")
    private String roleDesc;

    /**
     * 角色状态
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{valid.role.enabled.NotNull}")
    @ApiModelProperty(value = "角色状态")
    private Boolean enabled;
}
