package com.github.stazxr.zblog.base.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 权限查询
 *
 * @author SunTao
 * @since 2022-08-25
 */
@Getter
@Setter
@ToString
@ApiModel("权限查询参数")
public class PermissionQueryDto extends PageParam {
    /**
     * 权限id
     */
    @ApiModelProperty("权限id")
    private Long permId;

    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    private Long roleId;

    /**
     * 模糊查询（标题、权限标识、组件名称、组件路径）
     */
    @ApiModelProperty(value = "标题、权限标识、组件名称、组件路径查询", notes = "模糊查询")
    private String blurry;

    /**
     * 权限状态
     */
    @ApiModelProperty("权限状态")
    private Boolean enabled;

    /**
     * 是否外链
     */
    @ApiModelProperty(name = "iFrame", value = "是否外链")
    private Boolean iFrame;

    /**
     * 是否只显示菜单
     */
    @ApiModelProperty("是否只显示菜单")
    private Boolean onlyShowMenu;

    /**
     * 是否包含顶级菜单
     */
    @ApiModelProperty("是否包含顶级菜单")
    private Boolean needTop;
}
