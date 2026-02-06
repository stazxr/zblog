package com.github.stazxr.zblog.base.domain.dto;

import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.core.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * PermissionDto
 *
 * @author SunTao
 * @since 2022-06-29
 */
@Getter
@Setter
@ApiModel("权限DTO")
public class PermissionDto extends BaseDto {
    private static final long serialVersionUID = -7631261139679214658L;

    /**
     * 权限ID
     */
    @NotNull(groups = Update.class, message = "{TECH_PARAM_MISS}")
    @ApiModelProperty("权限id")
    private Long id;

    /**
     * 上级权限ID
     */
    @ApiModelProperty("权限pid")
    private Long pid;

    /**
     * 权限名称
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{PERM_NAME_REQUIRED}")
    @ApiModelProperty("权限名称")
    private String permName;

    /**
     * 权限编码
     */
    @ApiModelProperty("权限编码")
    private String permCode;

    /**
     * 权限类型
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{PERM_TYPE_REQUIRED}")
    @ApiModelProperty("权限类型，1：目录、2：菜单、3：按钮、4：外链")
    private Integer permType;

    /**
     * 权限访问级别
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{PERM_LEVEL_REQUIRED}")
    @ApiModelProperty("权限访问级别")
    private Integer permLevel;

    /**
     * 组件名称
     */
    @ApiModelProperty("组件名称")
    private String componentName;

    /**
     * 组件路径
     */
    @ApiModelProperty("组件路径")
    private String componentPath;

    /**
     * 路由地址
     */
    @ApiModelProperty("前端路由地址")
    private String routerPath;

    /**
     * 权限图标
     */
    @ApiModelProperty("权限图标")
    private String icon;

    /**
     * 权限排序
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{PARAM_SORT_REQUIRED}")
    @Min(value = 1, groups = {Create.class, Update.class}, message = "{PARAM_SORT_MIN1}")
    @Max(value =99999, groups = {Create.class, Update.class}, message = "{PARAM_SORT_MAX99999}")
    @ApiModelProperty(value = "权限排序", example = "99999")
    private Integer sort;

    /**
     * 是否缓存
     */
    @ApiModelProperty(value = "是否缓存")
    private Boolean cacheable;

    /**
     * 是否隐藏
     */
    @ApiModelProperty(value = "是否隐藏")
    private Boolean hidden;

    /**
     * 是否启用
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{PERM_ENABLED_REQUIRED}")
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;
}
