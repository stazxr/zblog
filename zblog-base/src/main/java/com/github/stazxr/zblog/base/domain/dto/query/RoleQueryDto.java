package com.github.stazxr.zblog.base.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 角色查询
 *
 * @author SunTao
 * @since 2022-08-29
 */
@Getter
@Setter
@ToString
@ApiModel("角色查询参数")
public class RoleQueryDto extends PageParam {
    /**
     * 权限id
     */
    @ApiModelProperty("权限id")
    private Long permId;

    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    private String roleName;

    /**
     * 角色编码
     */
    @ApiModelProperty("角色编码")
    private String roleCode;

    /**
     * 角色状态
     */
    @ApiModelProperty("角色状态")
    private Boolean enabled;
}
